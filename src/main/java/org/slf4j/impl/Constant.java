package org.slf4j.impl;

import java.util.HashMap;
import java.util.Map;

public interface Constant {

    String DATE_TIME_FORMAT_STR_DEFAULT = "yyyy/MM/dd HH:mm:ss";

    String CONFIGURATION_FILE = "app.properties";

    /**
     * All system properties used by <code>SimpleLogger</code> start with this
     * prefix
     */
    String SYSTEM_PREFIX = "com.blade.logger.";

    String LOG_ERR = "System.err";
    String LOG_OUT = "System.out";

    String LOG_KEY_PREFIX = SYSTEM_PREFIX;

    /**
     * com.blade.logger.cacheOutputStream
     */
    String CACHE_OUTPUT_STREAM_STRING_KEY = SYSTEM_PREFIX + "cacheOutputStream";

    /**
     * com.blade.logger.levelInBrackets
     */
    String LEVEL_IN_BRACKETS_KEY = SYSTEM_PREFIX + "levelInBrackets";

    /**
     * com.blade.logger.name
     */
    String LOG_NAME_KEY = SYSTEM_PREFIX + "name";

    /**
     * com.blade.logger.config
     */
    String LOG_CONFIG_KEY = SYSTEM_PREFIX + "config";

    /**
     * Blade app name key
     */
    String APP_NAME_KEY = "app.name";

    /**
     * com.blade.logger.dir
     */
    String LOG_DIR_KEY = SYSTEM_PREFIX + "dir";

    /**
     * com.blade.logger.maxSize
     */
    String MAX_SIZE_KEY = SYSTEM_PREFIX + "maxSize";

    /**
     * com.blade.logger.cacheSize
     */
    String CACHE_SIZE_KEY = SYSTEM_PREFIX + "cacheSize";

    /**
     * com.blade.logger.writeInterval
     */
    String WRITE_INTERVAL_KEY = SYSTEM_PREFIX + "writeInterval";

    /**
     * com.blade.logger.showShortName
     */
    String SHOW_SHORT_NAME_KEY = SYSTEM_PREFIX + "shortName";

    /**
     * com.blade.logger.showLogName
     */
    String SHOW_LOG_NAME_KEY = SYSTEM_PREFIX + "showLogName";

    /**
     * com.blade.logger.showThreadName
     */
    String SHOW_THREAD_NAME_KEY = SYSTEM_PREFIX + "showThread";

    /**
     * com.blade.logger.datePattern
     */
    String DATE_TIME_FORMAT_KEY = SYSTEM_PREFIX + "datePattern";

    /**
     * com.blade.logger.showDate
     */
    String SHOW_DATE_TIME_KEY = SYSTEM_PREFIX + "showDate";

    /**
     * com.blade.logger.console
     */
    String SHOW_CONSOLE_KEY = SYSTEM_PREFIX + "console";

    /**
     * com.blade.logger.rootLevel
     */
    String ROOT_LEVEL_KEY = SYSTEM_PREFIX + "rootLevel";

    /**
     * com.blade.logger.disableColor
     */
    String DISABLE_COLOR = SYSTEM_PREFIX + "disableColor";

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
    String INFO  = "info";
    String DEBUG = "debug";
    String WARN  = "warn";
    String ERROR = "error";
    String OFF   = "error";
}