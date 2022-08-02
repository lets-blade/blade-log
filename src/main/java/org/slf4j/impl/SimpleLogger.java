/**
 * Copyright (c) 2004-2012 QOS.ch
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.slf4j.impl;

import org.slf4j.Logger;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.impl.utils.LogUtils;
import org.slf4j.spi.LocationAwareLogger;

import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.slf4j.impl.Constant.LOG_DESC_MAP;

/**
 * <p>
 * Simple implementation of {@link Logger} that sends all enabled impl messages,
 * for all defined loggers, to the console ({@code System.err}). The following
 * system properties are supported to configure the behavior of this impl:
 * </p>
 * <p>
 * <ul>
 * <li><code>org.slf4j.simpleLogger.logFile</code> - The output target which can
 * be the <em>path</em> to a file, or the special values "System.out" and
 * "System.err". Default is "System.err".</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.cacheOutputStream</code> - If the output
 * target is set to "System.out" or "System.err" (see preceding entry), by
 * default, logs will be output to the latest value referenced by
 * <code>System.out/err</code> variables. By setting this
 * parameter to true, the output stream will be cached, i.e. assigned once at
 * initialization time and re-used independently of the current value referenced by
 * <code>System.out/err</code>.
 * </li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.defaultLogLevel</code> - Default impl level
 * for all instances of SimpleLogger. Must be one of ("trace", "debug", "info",
 * "warn", "error" or "off"). If not specified, defaults to "info".</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.impl.<em>a.b.c</em></code> - Logging detail
 * level for a SimpleLogger instance named "a.b.c". Right-side value must be one
 * of "trace", "debug", "info", "warn", "error" or "off". When a SimpleLogger
 * named "a.b.c" is initialized, its level is assigned from this property. If
 * unspecified, the level of nearest parent impl will be used, and if none is
 * set, then the value specified by
 * <code>org.slf4j.simpleLogger.defaultLogLevel</code> will be used.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.showDateTime</code> - Set to
 * <code>true</code> if you want the current date and time to be included in
 * output messages. Default is <code>false</code></li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.dateTimeFormat</code> - The date and time
 * format to be used in the output messages. The pattern describing the date and
 * time format is defined by <a href=
 * "http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html">
 * <code>SimpleDateFormat</code></a>. If the format is not specified or is
 * invalid, the number of milliseconds since start up will be output.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.showThreadName</code> -Set to
 * <code>true</code> if you want to output the current thread name. Defaults to
 * <code>true</code>.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.showLogName</code> - Set to
 * <code>true</code> if you want the Logger instance name to be included in
 * output messages. Defaults to <code>true</code>.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.showShortLogName</code> - Set to
 * <code>true</code> if you want the last component of the name to be included
 * in output messages. Defaults to <code>false</code>.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.levelInBrackets</code> - Should the level
 * string be output in brackets? Defaults to <code>false</code>.</li>
 * <p>
 * <li><code>org.slf4j.simpleLogger.warnLevelString</code> - The string value
 * output for the warn level. Defaults to <code>WARN</code>.</li>
 * <p>
 * </ul>
 * <p>
 * <p>
 * In addition to looking for system properties with the names specified above,
 * this implementation also checks for a class loader resource named
 * <code>"simplelogger.properties"</code>, and includes any matching definitions
 * from this resource (if it exists).
 * </p>
 * <p>
 * <p>
 * With no configuration, the default output includes the relative time in
 * milliseconds, thread name, the level, impl name, and the message followed
 * by the line separator for the host. In log4j terms it amounts to the "%r [%t]
 * %level %impl - %m%n" pattern.
 * </p>
 * <p>
 * Sample output follows.
 * </p>
 * <p>
 * <pre>
 * 176 [main] INFO examples.Sort - Populating an array of 2 elements in reverse order.
 * 225 [main] INFO examples.SortAlgo - Entered the sort method.
 * 304 [main] INFO examples.SortAlgo - Dump of integer array:
 * 317 [main] INFO examples.SortAlgo - Element [0] = 0
 * 331 [main] INFO examples.SortAlgo - Element [1] = 1
 * 343 [main] INFO examples.Sort - The next impl statement should be an error message.
 * 346 [main] ERROR examples.SortAlgo - Tried to dump an uninitialized array.
 *   at org.log4j.examples.SortAlgo.dump(SortAlgo.java:58)
 *   at org.log4j.examples.Sort.main(Sort.java:64)
 * 467 [main] INFO  examples.Sort - Exiting main method.
 * </pre>
 * <p>
 * <p>
 * This implementation is heavily inspired by
 * <a href="http://commons.apache.org/logging/">Apache Commons Logging</a>'s
 * SimpleLog.
 * </p>
 *
 * @author Ceki G&uuml;lc&uuml;
 * @author Scott Sanders
 * @author Rod Waldhoff
 * @author Robert Burrell Donkin
 * @author C&eacute;drik LIME
 */
public class SimpleLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = -632788891211436180L;

    static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
    static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
    static final int LOG_LEVEL_INFO  = LocationAwareLogger.INFO_INT;
    static final int LOG_LEVEL_WARN  = LocationAwareLogger.WARN_INT;
    static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

    /**
     * The OFF level can only be used in configuration files to disable logging.
     * It has no printing method associated with it in o.s.Logger interface.
     */
    protected static final int LOG_LEVEL_OFF = LOG_LEVEL_ERROR + 10;

    private static boolean                   INITIALIZED   = false;
    private static SimpleLoggerConfiguration CONFIG_PARAMS = null;

    static void lazyInit() {
        if (INITIALIZED) {
            return;
        }
        INITIALIZED = true;
        init();
    }

    /**
     * external software might be invoking this method directly. Do not rename or change its semantics.
     */
    static void init() {
        CONFIG_PARAMS = new SimpleLoggerConfiguration();
        CONFIG_PARAMS.init();
    }

    /**
     * The current impl level
     */
    protected int rootLevel;

    /**
     * The short name of this simple impl instance
     */
    private transient String shortLogName = null;

    /**
     * Package access allows only {@link SimpleLoggerFactory} to instantiate
     * SimpleLogger instances.
     */
    SimpleLogger(String name) {
        this.name = name;
        String levelString = recursivelyComputeLevelString();
        if (levelString != null) {
            this.rootLevel = SimpleLoggerConfiguration.stringToLevel(levelString);
        } else {
            this.rootLevel = CONFIG_PARAMS.defaultLogLevel;
        }
    }

    private String recursivelyComputeLevelString() {
        String tempName       = name;
        String levelString    = null;
        int    indexOfLastDot = tempName.length();
        while ((levelString == null) && (indexOfLastDot > -1)) {
            tempName = tempName.substring(0, indexOfLastDot);
            levelString = CONFIG_PARAMS.getStringProp(Constant.LOG_KEY_PREFIX + tempName, null);
            indexOfLastDot = String.valueOf(tempName).lastIndexOf(".");
        }
        return levelString;
    }

    /**
     * This is our internal implementation for logging regular
     * (non-parameterized) impl messages.
     *
     * @param level   One of the LOG_LEVEL_XXX constants defining the impl level
     * @param message The message itself
     * @param t       The exception whose stack trace should be logged
     */
    private void log(int level, String message, Throwable t) {
        if (!isLevelEnabled(level)) {
            return;
        }

        StringBuffer buf = new StringBuffer(message.length() + 110);

        // Append date-time if so configured
        if (CONFIG_PARAMS.showDateTime) {
            String datetime = getFormattedDate() + ' ';
            if (CONFIG_PARAMS.disableColor) {
                buf.append(datetime);
            } else {
                buf.append(Ansi.White.format(datetime));
            }
        }

        if (CONFIG_PARAMS.levelInBrackets) {
            buf.append("[ ");
        }

        // Append a readable representation of the impl level
        if (CONFIG_PARAMS.disableColor) {
            buf.append(LOG_DESC_MAP.get(level + 50));
        } else {
            buf.append(LOG_DESC_MAP.get(level));
        }

        if (CONFIG_PARAMS.levelInBrackets) {
            buf.append(" ]");
        }
        buf.append(' ');

        // Append current thread name if so configured
        if (CONFIG_PARAMS.showThreadName) {
            String threadName = CONFIG_PARAMS.disableColor ? LogUtils.getThreadPadding() : LogUtils.getColorThreadPadding();
            buf.append(threadName);
        }

        if(CONFIG_PARAMS.openTraceId){
            String traceId = MDC.get("traceId");
            if(null != traceId){
                buf.append("[traceId: ");
                buf.append(traceId);
                buf.append("] ");
            }
        }

        // Append the name of the impl instance if so configured
        if (CONFIG_PARAMS.showShortLogName) {
            if (shortLogName == null) {
                shortLogName = CONFIG_PARAMS.disableColor ? LogUtils.getShortName(name) : LogUtils.getColorShortName(name);
            }
            buf.append(shortLogName);
        } else if (CONFIG_PARAMS.showLogName) {
            buf.append(String.valueOf(name)).append(" | ");
        }

        // Append the message
        buf.append(message);

        write(buf, t);
    }

    private void write(StringBuffer buf, Throwable t) {
        if (CONFIG_PARAMS.outputChoice.outputChoiceType == OutputChoice.OutputChoiceType.FILE) {
            if (CONFIG_PARAMS.showConsole) {
                if (null != t) {
                    String stack = " " + LogUtils.stackTraceToString(t);
                    buf.append(stack);
                    System.err.println(buf.toString());
                } else {
                    System.out.println(buf.toString());
                    System.out.flush();
                }
            }
            if (null != CONFIG_PARAMS.fileRunner) {
                CONFIG_PARAMS.fileRunner.addToQueue(buf);
            }
        } else {
            PrintStream targetStream = CONFIG_PARAMS.outputChoice.getTargetPrintStream();

            targetStream.println(buf.toString());
            writeThrowable(t, targetStream);
            targetStream.flush();
        }
    }

    private void writeThrowable(Throwable t, PrintStream targetStream) {
        if (t != null) {
            t.printStackTrace();
            if (CONFIG_PARAMS.outputChoice.outputChoiceType == OutputChoice.OutputChoiceType.FILE) {
                t.printStackTrace(targetStream);
            }
        }
    }

    private String getFormattedDate() {
        return LocalDateTime.now().format(CONFIG_PARAMS.dateFormatter);
    }

    /**
     * For formatted messages, first substitute arguments and then impl.
     *
     * @param level
     * @param format
     * @param arg1
     * @param arg2
     */
    private void formatAndLog(int level, String format, Object arg1, Object arg2) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * For formatted messages, first substitute arguments and then impl.
     *
     * @param level
     * @param format
     * @param arguments a list of 3 ore more arguments
     */
    private void formatAndLog(int level, String format, Object... arguments) {
        if (!isLevelEnabled(level)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
        log(level, tp.getMessage(), tp.getThrowable());
    }

    /**
     * Is the given impl level currently enabled?
     *
     * @param logLevel is this level enabled?
     */
    private boolean isLevelEnabled(int logLevel) {
        // impl level are numerically ordered so can use simple numeric
        // comparison
        return (logLevel >= rootLevel);
    }

    /**
     * Are {@code trace} messages currently enabled?
     */
    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LOG_LEVEL_TRACE);
    }

    /**
     * A simple implementation which logs messages of level TRACE according to
     * the format outlined above.
     */
    @Override
    public void trace(String msg) {
        log(LOG_LEVEL_TRACE, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    @Override
    public void trace(String format, Object param1) {
        formatAndLog(LOG_LEVEL_TRACE, format, param1, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    @Override
    public void trace(String format, Object param1, Object param2) {
        formatAndLog(LOG_LEVEL_TRACE, format, param1, param2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * TRACE according to the format outlined above.
     */
    @Override
    public void trace(String format, Object... argArray) {
        formatAndLog(LOG_LEVEL_TRACE, format, argArray);
    }

    /**
     * Log a message of level TRACE, including an exception.
     */
    @Override
    public void trace(String msg, Throwable t) {
        log(LOG_LEVEL_TRACE, msg, t);
    }

    /**
     * Are {@code debug} messages currently enabled?
     */
    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LOG_LEVEL_DEBUG);
    }

    /**
     * A simple implementation which logs messages of level DEBUG according to
     * the format outlined above.
     */
    @Override
    public void debug(String msg) {
        log(LOG_LEVEL_DEBUG, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    @Override
    public void debug(String format, Object param1) {
        formatAndLog(LOG_LEVEL_DEBUG, format, param1, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    @Override
    public void debug(String format, Object param1, Object param2) {
        formatAndLog(LOG_LEVEL_DEBUG, format, param1, param2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * DEBUG according to the format outlined above.
     */
    @Override
    public void debug(String format, Object... argArray) {
        formatAndLog(LOG_LEVEL_DEBUG, format, argArray);
    }

    /**
     * Log a message of level DEBUG, including an exception.
     */
    @Override
    public void debug(String msg, Throwable t) {
        log(LOG_LEVEL_DEBUG, msg, t);
    }

    /**
     * Are {@code info} messages currently enabled?
     */
    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LOG_LEVEL_INFO);
    }

    /**
     * A simple implementation which logs messages of level INFO according to
     * the format outlined above.
     */
    @Override
    public void info(String msg) {
        log(LOG_LEVEL_INFO, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    @Override
    public void info(String format, Object arg) {
        formatAndLog(LOG_LEVEL_INFO, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    @Override
    public void info(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_INFO, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * INFO according to the format outlined above.
     */
    @Override
    public void info(String format, Object... argArray) {
        formatAndLog(LOG_LEVEL_INFO, format, argArray);
    }

    /**
     * Log a message of level INFO, including an exception.
     */
    @Override
    public void info(String msg, Throwable t) {
        log(LOG_LEVEL_INFO, msg, t);
    }

    /**
     * Are {@code warn} messages currently enabled?
     */
    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LOG_LEVEL_WARN);
    }

    /**
     * A simple implementation which always logs messages of level WARN
     * according to the format outlined above.
     */
    @Override
    public void warn(String msg) {
        log(LOG_LEVEL_WARN, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    @Override
    public void warn(String format, Object arg) {
        formatAndLog(LOG_LEVEL_WARN, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_WARN, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * WARN according to the format outlined above.
     */
    @Override
    public void warn(String format, Object... argArray) {
        formatAndLog(LOG_LEVEL_WARN, format, argArray);
    }

    /**
     * Log a message of level WARN, including an exception.
     */
    @Override
    public void warn(String msg, Throwable t) {
        log(LOG_LEVEL_WARN, msg, t);
    }

    /**
     * Are {@code error} messages currently enabled?
     */
    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LOG_LEVEL_ERROR);
    }

    /**
     * A simple implementation which always logs messages of level ERROR
     * according to the format outlined above.
     */
    @Override
    public void error(String msg) {
        log(LOG_LEVEL_ERROR, msg, null);
    }

    /**
     * Perform single parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    @Override
    public void error(String format, Object arg) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg, null);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    @Override
    public void error(String format, Object arg1, Object arg2) {
        formatAndLog(LOG_LEVEL_ERROR, format, arg1, arg2);
    }

    /**
     * Perform double parameter substitution before logging the message of level
     * ERROR according to the format outlined above.
     */
    @Override
    public void error(String format, Object... argArray) {
        formatAndLog(LOG_LEVEL_ERROR, format, argArray);
    }

    /**
     * Log a message of level ERROR, including an exception.
     */
    @Override
    public void error(String msg, Throwable t) {
        log(LOG_LEVEL_ERROR, msg, t);
    }

    public void log(LoggingEvent event) {
        int levelInt = event.getLevel().toInt();

        if (!isLevelEnabled(levelInt)) {
            return;
        }
        FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
        log(levelInt, tp.getMessage(), event.getThrowable());
    }

}
