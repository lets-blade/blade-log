package org.slf4j.impl.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author biezhi
 * @date 2018/3/29
 */
public class StringUtils {

    private static final DateTimeFormatter d1 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter d2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter d3 = DateTimeFormatter.ofPattern("HHmmss");

    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return null != str || str.length() > 0;
    }

    public static String getDate() {
        return LocalDate.now().format(d1);
    }

    public static String getNormalDate() {
        return LocalDate.now().format(d2);
    }

    public static String getTime() {
        return LocalTime.now().format(d3);
    }

    public static byte[] toBytes(String str) {
        try {
            if (str == null || str.length() <= 0) {
                return new byte[0];
            } else {
                return str.getBytes("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
