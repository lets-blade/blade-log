package org.slf4j.impl;

import java.util.HashMap;
import java.util.Map;

public interface Constant {

    String DATE_TIME_FORMAT_STR_DEFAULT = "yyyy/MM/dd HH:mm:ss";

    String CONFIGURATION_FILE = "application";
    String CONFIGURATION_FILE0 = "app";

    /**
     * All system properties used by <code>SimpleLogger</code> start with this
     * prefix
     */
    String SYSTEM_PREFIX = "logger.";

    String LOG_ERR = "System.err";
    String LOG_OUT = "System.out";

    String LOG_KEY_PREFIX = SYSTEM_PREFIX;

    /**
     * logger.cache-output-stream
     */
    String CACHE_OUTPUT_STREAM_STRING_KEY = SYSTEM_PREFIX + "cache-output-stream";

    /**
     * logger.level-in-brackets
     */
    String LEVEL_IN_BRACKETS_KEY = SYSTEM_PREFIX + "level-in-brackets";

    /**
     * logger.name
     */
    String LOG_NAME_KEY = SYSTEM_PREFIX + "name";

    /**
     * logger.config
     */
    String LOG_CONFIG_KEY = SYSTEM_PREFIX + "config";

    /**
     * Blade app name key
     */
    String APP_NAME_KEY = "app.name";

    /**
     * logger.dir
     */
    String LOG_DIR_KEY = SYSTEM_PREFIX + "dir";

    /**
     * logger.max-size
     */
    String MAX_SIZE_KEY = SYSTEM_PREFIX + "max-size";

    /**
     * logger.cache-size
     */
    String CACHE_SIZE_KEY = SYSTEM_PREFIX + "cache-size";

    /**
     * logger.write-interval
     */
    String WRITE_INTERVAL_KEY = SYSTEM_PREFIX + "write-interval";

    /**
     * logger.short-name
     */
    String SHOW_SHORT_NAME_KEY = SYSTEM_PREFIX + "short-name";

    /**
     * logger.show-log-name
     */
    String SHOW_LOG_NAME_KEY = SYSTEM_PREFIX + "show-log-name";

    /**
     * logger.show-thread-name
     */
    String SHOW_THREAD_NAME_KEY = SYSTEM_PREFIX + "show-thread";

    /**
     * logger.datePattern
     */
    String DATE_TIME_FORMAT_KEY = SYSTEM_PREFIX + "date-pattern";

    /**
     * logger.showDate
     */
    String SHOW_DATE_TIME_KEY = SYSTEM_PREFIX + "show-date";

    /**
     * logger.console
     */
    String SHOW_CONSOLE_KEY = SYSTEM_PREFIX + "console";

    /**
     * logger.root-level
     */
    String ROOT_LEVEL_KEY = SYSTEM_PREFIX + "root-level";

    /**
     * logger.disable-color
     */
    String DISABLE_COLOR = SYSTEM_PREFIX + "disable-color";

    /**
     * logger.open-trace-id
     */
    String OPEN_TRACE_ID = SYSTEM_PREFIX + "open-trace-id";

    Map<Integer, String> LOG_DESC_MAP = new HashMap<Integer, String>() {
        private static final long serialVersionUID = -8216579733086302246L;

        {
            put(0, Ansi.White.and(Ansi.Bold).format("TRACE"));
            put(10, Ansi.Cyan.and(Ansi.Bold).format("DEBUG"));
            put(20, Ansi.Green.and(Ansi.Bold).format(" INFO"));
            put(30, Ansi.Yellow.and(Ansi.Bold).format(" WARN"));
            put(40, Ansi.Red.and(Ansi.Bold).format("ERROR"));

            put(50, "TRACE");
            put(60, "DEBUG");
            put(70, " INFO");
            put(80, " WARN");
            put(90, "ERROR");
        }
    };

    String TRACE = "trace";
    String INFO = "info";
    String DEBUG = "debug";
    String WARN = "warn";
    String ERROR = "error";
    String OFF = "error";
}