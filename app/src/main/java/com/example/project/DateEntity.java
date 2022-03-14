package com.example.project;

/**
 * Define DateEntity class for calendar
 *
 * @author Dat Pham, Dhakeswor Nyaupane
 * @version 0.0.1
 * @since 2022-02-27
 */
public class DateEntity {
    private int date;
    private int month;
    private int year;
    private int weekDay;
    public boolean isSelected = false;
    public boolean isInCurrentMonth = true;
    public boolean isToday = false;

    /**
     * Constructor for DateEntity class
     *
     * @param year    assign year for dateEntity
     * @param month   assign month for dateEntity
     * @param date    assign date for dateEntity
     * @param weekDay assign weekDay as Integer for dateEntity
     */
    public DateEntity(int year, int month, int date, int weekDay) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.weekDay = weekDay;
    }

    /**
     * Get dateEntity's year
     *
     * @return int year of dateEntity
     */
    public int getYear() {
        return year;
    }

    /**
     * Get dateEntity's month
     *
     * @return int month of dateEntity
     */
    public int getMonth() {
        return month;
    }

    /**
     * Get dateEntity's date
     *
     * @return int date of dateEntity
     */
    public int getDate() {
        return date;
    }

    /**
     * Get dateEntity's week day
     *
     * @return int weekDay of dateEntity
     */
    public int getWeekDay() {
        return weekDay;
    }
}
