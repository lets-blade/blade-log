package org.slf4j.impl.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.impl.utils.ColorUtils.padLeft;

/**
 * Log Utils
 *
 * @author biezhi
 * @date 2018/3/29
 */
public class LogUtils {

    private static final DateTimeFormatter d1 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter d2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter d3 = DateTimeFormatter.ofPattern("HHmmss");

    private static Map<String, String> THREAD_NAME_CACHE = new HashMap<>();
    private static Map<String, String> CLASS_NAME_CACHE  = new HashMap<>();

    private static boolean isWindows;

    static {
        isWindows = System.getProperties().getProperty("os.name").toLowerCase().contains("win");
    }

    public static boolean isWindows() {
        return isWindows;
    }

    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return null != str || str.length() > 0;
    }

    public static String getDate() {
        return LocalDate.now().format(d1);
    }

    public static String getNormalDate() {
        return LocalDate.now().format(d2);
    }

    public static String getTime() {
        return LocalTime.now().format(d3);
    }

    public static String getShortName(String className) {
        if (CLASS_NAME_CACHE.containsKey(className)) {
            return CLASS_NAME_CACHE.get(className);
        }
        int           len          = 31;
        String[]      packageNames = className.split("\\.");
        StringBuilder shortName    = new StringBuilder();
        int           pos          = 0;
        for (String pkg : packageNames) {
            if (pos != packageNames.length - 1) {
                shortName.append(pkg.charAt(0)).append('.');
            } else {
                shortName.append(pkg);
            }
            pos++;
        }
        String val = padLeft(shortName.toString(), len);
        val = ColorUtils.blue(val + " : ");
        CLASS_NAME_CACHE.put(className, val);
        return val;
    }

    public static String getThreadPadding() {
        String key = Thread.currentThread().getName();
        if (THREAD_NAME_CACHE.containsKey(key)) {
            return THREAD_NAME_CACHE.get(key);
        }
        String val = "[ " + padLeft(Thread.currentThread().getName(), 17) + " ] ";
        val = ColorUtils.gray(val);
        THREAD_NAME_CACHE.put(key, val);
        return val;
    }

    public static byte[] toBytes(String str) {
        try {
            if (str == null || str.length() <= 0) {
                return new byte[0];
            } else {
                return str.getBytes("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

}
