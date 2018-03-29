package org.slf4j.impl.utils;

/**
 * Log color utils
 *
 * @author biezhi
 * @date 2018/3/28
 */
public final class ColorUtils {

    private static final String ANSI_RESET             = "\u001B[0m";
    private static final String ANSI_BLACK             = "\u001B[30m";
    private static final String ANSI_RED               = "\u001B[31m";
    private static final String ANSI_GREEN             = "\u001B[32m";
    private static final String ANSI_YELLOW            = "\u001B[33m";
    private static final String ANSI_BLUE              = "\u001B[34m";
    private static final String ANSI_PURPLE            = "\u001B[35m";
    private static final String ANSI_CYAN              = "\u001B[36m";
    private static final String ANSI_WHITE             = "\u001B[37m";
    private static final String ANSI_GRAY              = "\u001B[90m";
    private static final String ANSI_BLUE2             = "\u001B[94m";
    private static final String ANSI_BLACK_BACKGROUND  = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND    = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND  = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND   = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND   = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND  = "\u001B[47m";

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("win");
    }

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

    public static void blue(StringBuilder buf, String str) {
        if (isWindows()) {
            buf.append(str);
            return;
        }
        buf.append(ANSI_BLUE2).append(str).append(ANSI_RESET);
    }

    public static void gray(StringBuilder buf, String str) {
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
