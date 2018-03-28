package org.slf4j.impl;

import java.util.ArrayList;

/**
 * Logger item
 *
 * @author biezhi
 * @date 2018/3/28
 */
public class SimpleLoggerItem {

    String                   logFileName   = "";
    String                   logPath       = "";
    String                   lastWriteDate = "";
    long                     size          = 0;
    long                     nextWriteTime = 0;
    long                     cacheSize     = 0;
    char                     currLogBuff   = 'A';
    ArrayList<StringBuilder> alLogBufA     = new ArrayList<>();
    ArrayList<StringBuilder> alLogBufB     = new ArrayList<>();

}
