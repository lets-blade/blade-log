package org.slf4j.impl;

import org.slf4j.helpers.Util;

import java.io.*;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

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

    private static final String CONFIGURATION_FILE = "app.properties";

    private static int DEFAULT_LOG_LEVEL_DEFAULT = SimpleLogger.LOG_LEVEL_INFO;

    private static final String DATE_TIME_FORMAT_STR_DEFAULT = "yyyy/MM/dd HH:mm:ss";

    private static final boolean SHOW_DATE_TIME_DEFAULT      = true;
    private static final boolean SHOW_THREAD_NAME_DEFAULT    = true;
    private static final boolean SHOW_LOG_NAME_DEFAULT       = true;
    private static final boolean SHOW_SHORT_LOG_NAME_DEFAULT = true;
    private static final boolean CACHE_OUTPUT_STREAM_DEFAULT = false;
    private static final boolean LEVEL_IN_BRACKETS_DEFAULT   = false;

    DateTimeFormatter dateFormatter = null;
    OutputChoice      outputChoice  = null;

    boolean showLogName      = SHOW_LOG_NAME_DEFAULT;
    boolean showShortLogName = SHOW_SHORT_LOG_NAME_DEFAULT;
    boolean levelInBrackets  = LEVEL_IN_BRACKETS_DEFAULT;
    boolean showThreadName   = SHOW_THREAD_NAME_DEFAULT;
    boolean showDateTime     = SHOW_DATE_TIME_DEFAULT;
    int     defaultLogLevel  = DEFAULT_LOG_LEVEL_DEFAULT;

    private static String LOG_FILE_DEFAULT = "System.out";
    private        String logFile          = LOG_FILE_DEFAULT;

    private final Properties properties = new Properties();

    void init() {
        loadProperties();

        String defaultLogLevelString = getStringProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, null);
        if (defaultLogLevelString != null)
            defaultLogLevel = stringToLevel(defaultLogLevelString);

        showLogName = getBooleanProperty(SimpleLogger.SHOW_LOG_NAME_KEY, SimpleLoggerConfiguration.SHOW_LOG_NAME_DEFAULT);
        showShortLogName = getBooleanProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, SHOW_SHORT_LOG_NAME_DEFAULT);
        showDateTime = getBooleanProperty(SimpleLogger.SHOW_DATE_TIME_KEY, SHOW_DATE_TIME_DEFAULT);
        showThreadName = getBooleanProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, SHOW_THREAD_NAME_DEFAULT);
        String dateTimeFormatStr = getStringProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, DATE_TIME_FORMAT_STR_DEFAULT);
        levelInBrackets = getBooleanProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, LEVEL_IN_BRACKETS_DEFAULT);

        logFile = getStringProperty(SimpleLogger.LOG_FILE_KEY, logFile);

        boolean cacheOutputStream = getBooleanProperty(SimpleLogger.CACHE_OUTPUT_STREAM_STRING_KEY, CACHE_OUTPUT_STREAM_DEFAULT);
        outputChoice = computeOutputChoice(logFile, cacheOutputStream);

        if (dateTimeFormatStr != null) {
            try {
                dateFormatter = DateTimeFormatter.ofPattern(dateTimeFormatStr);
            } catch (IllegalArgumentException e) {
                Util.report("Bad date format in " + CONFIGURATION_FILE + "; will output relative time", e);
            }
        }
    }

    private void loadProperties() {
        // Add props from the resource simplelogger.properties
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

    String getStringProperty(String name, String defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }

    private boolean getBooleanProperty(String name, boolean defaultValue) {
        String prop = getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }

    private String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            // Ignore
        }
        return (prop == null) ? properties.getProperty(name) : prop;
    }

    static int stringToLevel(String levelStr) {
        if ("trace".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_TRACE;
        } else if ("debug".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_DEBUG;
        } else if ("info".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_INFO;
        } else if ("warn".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_WARN;
        } else if ("error".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_ERROR;
        } else if ("off".equalsIgnoreCase(levelStr)) {
            return SimpleLogger.LOG_LEVEL_OFF;
        }
        // assume INFO by default
        return SimpleLogger.LOG_LEVEL_INFO;
    }

    private static OutputChoice computeOutputChoice(String logFile, boolean cacheOutputStream) {
        if ("System.err".equalsIgnoreCase(logFile))
            if (cacheOutputStream)
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_ERR);
            else
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
        else if ("System.out".equalsIgnoreCase(logFile)) {
            if (cacheOutputStream)
                return new OutputChoice(OutputChoice.OutputChoiceType.CACHED_SYS_OUT);
            else
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_OUT);
        } else {
            try {
                if (!new File(logFile).getParentFile().exists()) {
                    new File(logFile).getParentFile().mkdirs();
                }
                FileOutputStream fos         = new FileOutputStream(logFile);
                PrintStream      printStream = new PrintStream(fos);
                return new OutputChoice(printStream);
            } catch (FileNotFoundException e) {
                Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
                return new OutputChoice(OutputChoice.OutputChoiceType.SYS_ERR);
            }
        }
    }

}
