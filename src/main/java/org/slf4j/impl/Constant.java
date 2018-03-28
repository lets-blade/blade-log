package org.slf4j.impl;

/**
 * 常量
 *
 * @author yunfeng.cheng
 * @version 2015/10/31
 */
public final class Constant {

    /**
     * All system properties used by <code>SimpleLogger</code> start with this
     * prefix
     */
    public static final String SYSTEM_PREFIX = "com.blade.logger.";

    public static final String LOG_KEY_PREFIX                 = SYSTEM_PREFIX;
    // com.blade.logger.cacheOutputStream
    public static final String CACHE_OUTPUT_STREAM_STRING_KEY = SYSTEM_PREFIX + "cacheOutputStream";
    // com.blade.logger.levelInBrackets
    public static final String LEVEL_IN_BRACKETS_KEY          = SYSTEM_PREFIX + "levelInBrackets";
    // com.blade.logger.name
    public static final String LOG_NAME_KEY                   = SYSTEM_PREFIX + "name";
    // com.blade.logger.path
    public static final String LOG_PATH_KEY                   = SYSTEM_PREFIX + "path";
    // com.blade.logger.maxSize
    public static final String MAX_SIZE_KEY                   = SYSTEM_PREFIX + "maxSize";
    // com.blade.logger.cacheSize
    public static final String CACHE_SIZE_KEY                 = SYSTEM_PREFIX + "cacheSize";
    // com.blade.logger.writeInterval
    public static final String WRITE_INTERVAL_KEY             = SYSTEM_PREFIX + "writeInterval";
    // com.blade.logger.showShortName
    public static final String SHOW_SHORT_NAME_KEY            = SYSTEM_PREFIX + "shortName";
    // com.blade.logger.showLogName
    public static final String SHOW_LOG_NAME_KEY              = SYSTEM_PREFIX + "showLogName";
    // com.blade.logger.showThreadName
    public static final String SHOW_THREAD_NAME_KEY           = SYSTEM_PREFIX + "showThreadName";
    // com.blade.logger.dateTimeFormat
    public static final String DATE_TIME_FORMAT_KEY           = SYSTEM_PREFIX + "dateTimeFormat";
    // com.blade.logger.showDateTime
    public static final String SHOW_DATE_TIME_KEY             = SYSTEM_PREFIX + "showDateTime";
    // com.blade.logger.defaultLogLevel
    public static final String ROOT_LEVEL_KEY                 = SYSTEM_PREFIX + "rootLevel";


}