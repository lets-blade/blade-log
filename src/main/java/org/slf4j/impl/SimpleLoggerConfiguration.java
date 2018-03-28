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

    private static final String     DATE_TIME_FORMAT_STR_DEFAULT = "yyyy/MM/dd HH:mm:ss";
    private final        Properties properties                   = new Properties();

    DateTimeFormatter dateFormatter = null;
    OutputChoice      outputChoice  = null;

    boolean showLogName      = false;
    boolean showShortLogName = true;
    boolean levelInBrackets  = false;
    boolean showThreadName   = true;
    boolean showDateTime     = true;
    int     defaultLogLevel  = SimpleLogger.LOG_LEVEL_INFO;


    private String logFile = "System.out";

    void init() {
        loadProperties();

        String defaultLogLevelString = getStringProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, null);
        if (defaultLogLevelString != null)
            defaultLogLevel = stringToLevel(defaultLogLevelString);

        showLogName = getBooleanProperty(SimpleLogger.SHOW_LOG_NAME_KEY, showLogName);
        showShortLogName = getBooleanProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, showShortLogName);
        showDateTime = getBooleanProperty(SimpleLogger.SHOW_DATE_TIME_KEY, showDateTime);
        showThreadName = getBooleanProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, showThreadName);
        String dateTimeFormatStr = getStringProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, DATE_TIME_FORMAT_STR_DEFAULT);
        levelInBrackets = getBooleanProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, levelInBrackets);

        logFile = getStringProperty(SimpleLogger.LOG_FILE_KEY, logFile);

        boolean cacheOutputStream = getBooleanProperty(SimpleLogger.CACHE_OUTPUT_STREAM_STRING_KEY, false);
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
