package com.example.project.utils;

import java.text.DateFormatSymbols;

/**
 * Utility for handling and formatting datetime value
 *
 * @author Dat Pham, Dhakeswor Nyaupane
 * @version 0.0.1
 * @since 2022-03-01
 */
public class DateTimeUtils {
    /**
     * Format value in type "XX" instead of "X"
     *
     * @param value datetime value as in Integer
     * @return String value in correct format
     */
    private static String formatValue(int value) {
        return (value < 10 ? "0" : "") + value;
    }

    /**
     * Format date value
     *
     * @param year  Integer value of year
     * @param month Integer value of month
     * @param date  Integer value of date
     * @return String value format "yyyy-MM-dd"
     */
    public static String buildDate(int year, int month, int date) {
        return year + "-" + formatValue(month) + "-" + formatValue(date);
    }

    /**
     * Format month and year values
     *
     * @param year  Integer value of year
     * @param month Integer value of month
     * @return String value format "MMMM yyyy"
     */
    public static String buildMonthYear(int year, int month) {
        return new DateFormatSymbols().getMonths()[month - 1] + " " + year;
    }

    /**
     * Format time value
     *
     * @param hour   Integer value of hour
     * @param minute Integer value of minute
     * @return String value format "hh-mm"
     */
    public static String buildTime(int hour, int minute) {
        return formatValue(hour) + ":" + formatValue(minute);
    }
}
