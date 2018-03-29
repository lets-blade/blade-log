package org.slf4j.impl;

import org.slf4j.impl.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Write log task
 *
 * @author biezhi
 * @date 2018/3/28
 */
public class WriterTask implements Runnable {

    private boolean isRunning      = true;
    private Lock    lock           = new ReentrantLock();
    private Lock    singleFileLock = new ReentrantLock();

    private Map<String, SimpleLoggerItem> logItemMap = new ConcurrentHashMap<>(8);

    private final String logDir;
    // 100MB
    private final long   maxSize;
    // 10KB
    private final long   cacheSize;
    // 1000ms
    private final long   writeInterval;

    public WriterTask(String logDir, long maxSize, long cacheSize, long writeInterval) {
        this.logDir = logDir;
        this.maxSize = maxSize;
        this.cacheSize = cacheSize;
        this.writeInterval = writeInterval;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                // write to file
                flush(false);
            } catch (Exception e) {
                System.err.println("Start logging error...");
                e.printStackTrace();
            }
        }
    }

    /**
     * Add log to queue
     *
     * @param logFileName
     * @param logMsg
     */
    public void addToQueue(String logFileName, StringBuilder logMsg) {
        // remove color code
        String newMsg = logMsg.toString().replaceAll("\u001B\\[\\d+m", "");
        logMsg = new StringBuilder(newMsg).append("\r\n");
        SimpleLoggerItem lfi = logItemMap.get(logFileName);
        if (lfi == null) {
            lock.lock();
            try {
                lfi = logItemMap.get(logFileName);
                if (lfi == null) {
                    lfi = new SimpleLoggerItem();
                    lfi.logFileName = logFileName;
                    lfi.nextWriteTime = System.currentTimeMillis() + writeInterval;
                    logItemMap.put(logFileName, lfi);
                }
            } finally {
                lock.unlock();
            }
        }
        // synchronize the log of a single file
        singleFileLock.lock();
        try {
            if (lfi.currLogBuff == 'A') {
                lfi.alLogBufA.add(logMsg);
            } else {
                lfi.alLogBufB.add(logMsg);
            }
            lfi.cacheSize += Objects.requireNonNull(LogUtils.toBytes(logMsg.toString())).length;
        } finally {
            singleFileLock.unlock();
        }
    }

    public void close() {
        isRunning = false;
        try {
            flush(true);
        } catch (Exception e) {
            System.err.println("Close logging error.");
            e.printStackTrace();
        }
    }

    private void flush(boolean bIsForce) throws IOException {
        long currTime = System.currentTimeMillis();
        for (String s : logItemMap.keySet()) {
            SimpleLoggerItem loggerItem = logItemMap.get(s);
            if (currTime >= loggerItem.nextWriteTime || cacheSize <= loggerItem.cacheSize || bIsForce) {
                this.flushLogger(loggerItem);
            } else {
                LogUtils.sleep(10);
            }
        }
    }

    private void flushLogger(SimpleLoggerItem loggerItem) throws IOException {
        lock.lock();
        ArrayList<StringBuilder> alWrtLog;
        try {
            if (loggerItem.currLogBuff == 'A') {
                alWrtLog = loggerItem.alLogBufA;
                loggerItem.currLogBuff = 'B';
            } else {
                alWrtLog = loggerItem.alLogBufB;
                loggerItem.currLogBuff = 'A';
            }
            loggerItem.cacheSize = 0;
        } finally {
            lock.unlock();
        }
        this.createLogFile(loggerItem);
        int iWriteSize = writeToFile(loggerItem.logPath, alWrtLog);
        loggerItem.size += iWriteSize;
    }

    private void createLogFile(SimpleLoggerItem loggerItem) {
        String currPCDate = LogUtils.getNormalDate();
        // Determine if the log root path exists. If it does not exist, create it first
        File rootDir = new File(logDir);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            rootDir.mkdirs();
        }

        // Cut by day
        if (!LogUtils.isEmpty(loggerItem.logPath) && !loggerItem.lastWriteDate.equals(currPCDate)) {

            String  yesterday = loggerItem.logFileName + "_" + loggerItem.lastWriteDate + ".log";
            boolean renamed   = new File(loggerItem.logPath).renameTo(new File(rootDir, yesterday));
            if (renamed) {
                loggerItem.logPath = "";
                loggerItem.lastWriteDate = "";
            }
        }

        // if you exceed a single file size, split the file
        if (LogUtils.isNotEmpty(loggerItem.logPath) && loggerItem.size >= maxSize) {
            File oldFile = new File(loggerItem.logPath);
            if (oldFile.exists()) {
                String  newFileName = logDir + "/" + loggerItem.logFileName + "_" + LogUtils.getDate() + "_" + LogUtils.getTime() + ".log";
                File    newFile     = new File(newFileName);
                boolean flag        = oldFile.renameTo(newFile);
                if (!flag) {
                    System.err.println("backup [" + newFile.getName() + "] fail.");
                }
                loggerItem.logPath = "";
                loggerItem.size = 0;
            }
        }

        // create log file
        if (LogUtils.isEmpty(loggerItem.logPath) || !loggerItem.lastWriteDate.equals(currPCDate)) {
            String sDir = logDir;
            File   file = new File(sDir);
            if (!file.exists()) {
                file.mkdir();
            }
            loggerItem.logPath = sDir + "/" + loggerItem.logFileName + ".log";
            loggerItem.lastWriteDate = currPCDate;

            file = new File(loggerItem.logPath);
            if (file.exists()) {
                loggerItem.size = file.length();
            } else {
                loggerItem.size = 0;
            }
        }
    }

    private int writeToFile(String sFullFileName, ArrayList<StringBuilder> sbLogMsg) throws IOException {
        int          size = 0;
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(sFullFileName, true);
            for (int i = 0; i < sbLogMsg.size(); i++) {
                StringBuilder logMsg   = sbLogMsg.get(i);
                byte[]        tmpBytes = LogUtils.toBytes(logMsg.toString());
                fout.write(tmpBytes);
                size += tmpBytes.length;
            }
            fout.flush();
            sbLogMsg.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                fout.close();
            }
        }
        return size;
    }

}
