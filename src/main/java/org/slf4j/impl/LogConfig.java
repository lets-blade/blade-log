package org.slf4j.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Log config
 *
 * @author biezhi
 * @date 2018/3/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogConfig {

    private Long    maxSize;
    private Long    cacheSize;
    private Long    writeInterval;
    private Integer rootLevel;
    private String  logDir;
    private Boolean showLogName;
    private Boolean shortLogName;
    private Boolean levelInBrackets;
    private Boolean showThread;
    private Boolean showDate;
    private Boolean showConsole;
}
