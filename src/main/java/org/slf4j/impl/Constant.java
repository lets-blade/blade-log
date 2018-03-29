package org.slf4j.impl;

public interface Constant {

    /**
     * All system properties used by <code>SimpleLogger</code> start with this
     * prefix
     */
    String SYSTEM_PREFIX = "com.blade.logger.";

    String LOG_KEY_PREFIX                 = SYSTEM_PREFIX;
    // com.blade.logger.cacheOutputStream
    String CACHE_OUTPUT_STREAM_STRING_KEY = SYSTEM_PREFIX + "cacheOutputStream";
    // com.blade.logger.levelInBrackets
    String LEVEL_IN_BRACKETS_KEY          = SYSTEM_PREFIX + "levelInBrackets";
    // com.blade.logger.name
    String LOG_NAME_KEY                   = SYSTEM_PREFIX + "name";
    // com.blade.logger.path
    String LOG_PATH_KEY                   = SYSTEM_PREFIX + "path";
    // com.blade.logger.maxSize
    String MAX_SIZE_KEY                   = SYSTEM_PREFIX + "maxSize";
    // com.blade.logger.cacheSize
    String CACHE_SIZE_KEY                 = SYSTEM_PREFIX + "cacheSize";
    // com.blade.logger.writeInterval
    String WRITE_INTERVAL_KEY             = SYSTEM_PREFIX + "writeInterval";
    // com.blade.logger.showShortName
    String SHOW_SHORT_NAME_KEY            = SYSTEM_PREFIX + "shortName";
    // com.blade.logger.showLogName
    String SHOW_LOG_NAME_KEY              = SYSTEM_PREFIX + "showLogName";
    // com.blade.logger.showThreadName
    String SHOW_THREAD_NAME_KEY           = SYSTEM_PREFIX + "showThreadName";
    // com.blade.logger.dateTimeFormat
    String DATE_TIME_FORMAT_KEY           = SYSTEM_PREFIX + "dateTimeFormat";
    // com.blade.logger.showDateTime
    String SHOW_DATE_TIME_KEY             = SYSTEM_PREFIX + "showDateTime";
    // com.blade.logger.defaultLogLevel
    String ROOT_LEVEL_KEY                 = SYSTEM_PREFIX + "rootLevel";

}