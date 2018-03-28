package org.slf4j.impl.utils;

/**
 * Log color utils
 *
 * @author biezhi
 * @date 2018/3/28
 */
public class ColorUtils {

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toLowerCase().contains("win");
    }

    public static String green(String str) {
        if (isWindows()) return str;
        return "\033[32m" + str + "\033[0m";
    }

    public static String red(String str) {
        if (isWindows()) return str;
        return "\033[31m" + str + "\033[0m";
    }

    public static String yellow(String str) {
        if (isWindows()) return str;
        return "\033[33m" + str + "\033[0m";
    }

    public static String gray(String str) {
        if (isWindows()) return str;
        return "\033[37m" + str + "\033[0m";
    }

    public static void blue(StringBuilder buf, String str) {
        if (isWindows()) {
            buf.append(str);
            return;
        }
        buf.append("\033[94m").append(str).append("\033[0m");
    }

    public static void gray(StringBuilder buf, String str) {
        if (isWindows()) {
            buf.append(str);
            return;
        }
        buf.append("\033[90m").append(str).append("\033[0m");
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

}
