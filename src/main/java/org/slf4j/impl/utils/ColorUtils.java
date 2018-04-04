package org.slf4j.impl.utils;

import static org.slf4j.impl.utils.LogUtils.isWindows;

/**
 * Log color utils
 *
 * @author biezhi
 * @date 2018/3/28
 */
public final class ColorUtils {

    private static final String ANSI_RESET             = "\u001B[0m";
    private static final String ANSI_RED               = "\u001B[31m";
    private static final String ANSI_GREEN             = "\u001B[32m";
    private static final String ANSI_YELLOW            = "\u001B[33m";
    private static final String ANSI_WHITE             = "\u001B[37m";
    private static final String ANSI_GRAY              = "\u001B[90m";
    private static final String ANSI_BLUE2             = "\u001B[94m";

    public static String green(String str) {
        if (isWindows()) return str;
        return ANSI_GREEN + str + ANSI_RESET;
    }

    public static String red(String str) {
        if (isWindows()) return str;
        return ANSI_RED + str + ANSI_RESET;
    }

    public static String yellow(String str) {
        if (isWindows()) return str;
        return ANSI_YELLOW + str + ANSI_RESET;
    }

    public static String gray(String str) {
        if (isWindows()) return str;
        return ANSI_WHITE + str + ANSI_RESET;
    }

    public static String blue(String str) {
        if (isWindows()) return str;
        return ANSI_BLUE2 + str + ANSI_RESET;
    }

    public static void blue(StringBuffer buf, String str) {
        if (isWindows()) {
            buf.append(str);
            return;
        }
        buf.append(ANSI_BLUE2).append(str).append(ANSI_RESET);
    }

    public static void gray(StringBuffer buf, String str) {
        if (isWindows()) {
            buf.append(str);
            return;
        }
        buf.append(ANSI_GRAY).append(str).append(ANSI_RESET);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

}
