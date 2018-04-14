package org.slf4j.impl;

import org.slf4j.helpers.Util;
import org.slf4j.impl.utils.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.slf4j.impl.Constant.*;

/**
 * This class holds configuration values for {@link SimpleLogger}. The
 * values are computed at runtime. See {@link SimpleLogger} documentation for
 * more information.
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott Sanders
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @author C&eacute;drik LIME
 * @since 1.7.25
 */
public class SimpleLoggerConfiguration {

    private final Properties properties = new Properties();

    DateTimeFormatter dateFormatter = null;
    OutputChoice      outputChoice  = null;

    boolean showLogName      = false;
    boolean showShortLogName = true;
    boolean levelInBrackets  = false;
    boolean showThreadName   = true;
    boolean showDateTime     = true;
    boolean showConsole      = true;
    int     defaultLogLevel  = SimpleLogger.LOG_LEVEL_INFO;

    FileRunner fileRunner;

    void init() {
        loadProperties();

        String defaultLogLevelString = getStringProp(Constant.ROOT_LEVEL_KEY, null);
        if (defaultLogLevelString != null) {
            defaultLogLevel = stringToLevel(defaultLogLevelString);
        }

        this.showLogName = getBoolProp(Constant.SHOW_LOG_NAME_KEY, showLogName);
        this.showShortLogName = getBoolProp(Constant.SHOW_SHORT_NAME_KEY, showShortLogName);
        this.showDateTime = getBoolProp(Constant.SHOW_DATE_TIME_KEY, showDateTime);
        this.showThreadName = getBoolProp(Constant.SHOW_THREAD_NAME_KEY, showThreadName);
        this.showConsole = getBoolProp(Constant.SHOW_CONSOLE_KEY, showConsole);

        String dateTimeFormatStr = getStringProp(Constant.DATE_TIME_FORMAT_KEY, DATE_TIME_FORMAT_STR_DEFAULT);
        this.levelInBrackets = getBoolProp(Constant.LEVEL_IN_BRACKETS_KEY, levelInBrackets);

        boolean cacheOutputStream = getBoolProp(Constant.CACHE_OUTPUT_STREAM_STRING_KEY, false);

        String logDir = getStringProp(Constant.LOG_DIR_KEY, "");
        if (LogUtils.isEmpty(logDir)) {
            this.outputChoice = computeOutputChoice(logDir, cacheOutputStream);
        } else {

            if (logDir.endsWith(".jar")) {
                logDir = System.getenv("user.dir");
            }

            String logName = getStringProp(Constant.LOG_NAME_KEY, "");
            if (logName.isEmpty()) {
                logName = getStringProp(Constant.APP_NAME_KEY, logName);
            }
            if (logName.isEmpty()) {
                logName = "app";
            }
            String logName1 = logName;

            // 100MB
            long maxSize = getLongProp(Constant.MAX_SIZE_KEY, 1024 * 1024 * 100);

            String logFilePath = logDir + File.separator + logName;
            outputChoice = computeOutputChoice(logFilePath, cacheOutputStream);

            fileRunner = new FileRunner(logName, logDir, maxSize);

            Thread thread = new Thread(fileRunner);
            thread.setName("blade-logging");
            thread.setDaemon(true);
            thread.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> fileRunner.close()));
        }

        if (dateTimeFormatStr != null) {
            try {
                dateFormatter = DateTimeFormatter.ofPattern(dateTimeFormatStr);
            } catch (IllegalArgumentException e) {
                Util.report("Bad date format in " + CONFIGURATION_FILE + "; will output relative time", e);
            }
        }
    }

    private void loadProperties() {
        // Add props from the resource app.properties
        InputStream in = AccessController.doPrivileged((PrivilegedAction<InputStream>) () -> {
            ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
            if (threadCL != null) {
                return threadCL.getResourceAsStream(CONFIGURATION_FILE);
            } else {
                return ClassLoader.getSystemResourceAsStream(CONFIGURATION_FILE);
            }
        });
        if (null != in) {
            try {
                properties.load(in);
            } catch (java.io.IOException e) {
                // ignored
            } finally {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                    // ignored
                }
            }
        }
    }

    private Long getLongProp(String name, long defaultValue) {
        String val = getStringProp(name);
        if (null == val || val.isEmpty()) {
            return defaultValue;
        }
        return Long.parseLong(val);
    }

    String getStringProp(String name, String defaultValue) {
        String prop = getStringProp(name);
        return (prop == null) ? defaultValue : prop;
    }

    private boolean getBoolProp(String name, boolean defaultValue) {
        String prop = getStringProp(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    private String getStringProp(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            // Ignore
        }
        return (prop == null) ? properties.getProperty(name) : prop;
    }

    static int stringToLevel(String levelStr) {
        if (TRACE.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_TRACE;
        } else if (DEBUG.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_DEBUG;
        } else if (INFO.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_INFO;
        } else if (WARN.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_WARN;
        } else if (ERROR.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_ERROR;
        } else if (OFF.equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_OFF;
        }
        // assume INFO by default
        return SimpleLogger.LOG_LEVEL_INFO;
    }

    private static OutputChoice computeOutputChoice(String logFilePath, boolean cacheOutputStream) {
        if (LOG_ERR.equalsIgnoreCase(logFilePath)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
            } else {
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
            }
        } else if (LOG_OUT.equalsIgnoreCase(logFilePath)) {
            if (cacheOutputStream) {
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
            } else {
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
            }
        } else {
            return new OutputChoice(OutputChoice.OutputChoiceType.FILE);
        }
    }

}
