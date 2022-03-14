package com.example.project.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.project.DateEntity;
import com.example.project.activities.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Utility for setting up and managing calendar
 *
 * @author Dat Pham, Tran Cong Minh
 * @version 0.0.1
 * @since 2022-03-01
 */
public class CalendarUtils {
    private final Context mContext;
    private ArrayList<DateEntity> mDateEntities = new ArrayList<>();

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDate;

    private final int mTodayYear;
    private final int mTodayMonth;
    private final int mTodayDate;

    public static final int CAL_MONTHLY = 0;
    public static final int CAL_WEEKLY = 1;

    public int getTodayYear() {
        return mTodayYear;
    }

    /* An array of months' days in normal year */
    int[] commonYearMonthDay = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
     /* An array of months' days in leap year */
    int[] leapYearMonthDay = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    /**
     * Constructor for CalendarUtils.
     * Set context and year, month ,date values of today to variables.
     * Call initTodayDateList() function to set up mDateEntities of today.
     *
     * @param context Context
     */
    public CalendarUtils(Context context) {
        Calendar calendar = Calendar.getInstance();
        mContext = context;
        mTodayYear = calendar.get(Calendar.YEAR);
        mTodayMonth = calendar.get(Calendar.MONTH) + 1;
        mTodayDate = calendar.get(Calendar.DAY_OF_MONTH);
        initTodayDateList();
    }

    /**
     * Get ArrayList of DateEntity
     *
     * @return ArrayList dateEntities
     */
    public ArrayList<DateEntity> getDateEntities() {
        return mDateEntities;
    }

    /**
     * Get current datetime using DateTimeUtils
     *
     * @return String datetime in format "yyyy-MM-dd"
     */
    public String getCurrentDateTime() {
        return DateTimeUtils.buildDate(mCurrentYear, mCurrentMonth, mCurrentDate);
    }

    /**
     * Get current month and year using DateTimeUtils
     *
     * @return String value in format "MMMM yyyy"
     */
    public String getCurrentMonthYear() {
        return DateTimeUtils.buildMonthYear(mCurrentYear, mCurrentMonth);
    }

    /**
     * Initialize date list of today.
     * Function populateDateList is called with today values.
     */
    public void initTodayDateList() {
        mCurrentDate = mTodayDate;
        populateDateList(mTodayYear, mTodayMonth, mTodayDate);
    }

    /**
     * Initialize calendar date list by weekly view.
     *
     * @param year  The designated year to populate
     * @param month The designated month to populate
     * @param date  The designated date to populate
     *              
     * @return ArrayList<DateEntity> the list of DateEntity for designated week
     */
    public ArrayList<DateEntity> initWeeklyDateList(int year, int month, int date) {
        ArrayList<DateEntity> dateEntities = initDateList(year, month, date);
        ArrayList<DateEntity> weeklyDateEntities = new ArrayList<>();

        int index = 0;
        DateEntity entity = dateEntities.get(index);

        while (index < dateEntities.size()) {
            entity = dateEntities.get(index);
            if (entity.getYear() == year && entity.getMonth() == month && entity.getDate() == date) {
                break;
            }
            index++;
        }

        for (int i = 0; i <= entity.getWeekDay(); i++) {
            weeklyDateEntities.add(
                    dateEntities.get(index - (entity.getWeekDay() - i))
            );
        }

        for (int i = entity.getWeekDay() + 1; i < 7; i++) {
            weeklyDateEntities.add(
                    dateEntities.get(index + (i - entity.getWeekDay()))
            );
        }

        return weeklyDateEntities;
    }

    /**
     * Populate dates either in weekly or monthly view.
     * View type is checked from SharedPreferences.
     *
     * @param year  The designated year to populate
     * @param month The designated month to populate
     * @param date  The designated date to populate
     */
    public void populateDateList(int year, int month, int date) {
        SharedPreferences sp = mContext.getSharedPreferences(MainActivity.PREF_FILE_NAME, Activity.MODE_PRIVATE);
        int calType = sp.getInt(MainActivity.PREF_CAL_TYPE, 0);

        if (calType == CAL_MONTHLY) {
            mDateEntities = initDateList(year, month, date);
        } else if (calType == CAL_WEEKLY) {
            mDateEntities = initWeeklyDateList(year, month, date);
        }
    }

    /**
     * Populate all dates in the monthly view.
     * Set current year, month, date to the correct variables.
     *
     * @param year  The designated year to populate
     * @param month The designated month to populate
     * @param date  The designated date to populate
     * @return ArrayList all dateEntities of current month and year
     */
    private ArrayList<DateEntity> initDateList(int year, int month, int date) {
        ArrayList<DateEntity> dateEntities = new ArrayList<>();

        mCurrentYear = year;// The year on the current calendar
        mCurrentMonth = month;// Months on the current calendar
        mCurrentDate = date;

        int days = getMonthDays(year, month);// Get the total number of days this month
        int firstWeekDayOfCurrentMonth = getWeekDay(year, month);
        int lastWeekDayOfCurrentMonth = 0;

        int yearOfPreviousMonth = year;
        int previousMonth = month - 1;
        if (month == 1) {
            yearOfPreviousMonth = year - 1;
            previousMonth = 12;
        }

        int endDateOfPreviousMonth = getMonthDays(yearOfPreviousMonth, previousMonth);

        if (firstWeekDayOfCurrentMonth != 0) {
            int startDateOfPreviousMonth = endDateOfPreviousMonth - firstWeekDayOfCurrentMonth + 1;
            for (int i = startDateOfPreviousMonth, j = 0; i <= endDateOfPreviousMonth; i++, j++) {
                DateEntity dateEntity = new DateEntity(yearOfPreviousMonth, previousMonth, i, j % 7);
                dateEntity.isInCurrentMonth = false;
                dateEntities.add(dateEntity);
            }
        }

        for (int i = 1, j = firstWeekDayOfCurrentMonth; i <= days; i++, j++) {
            DateEntity dateEntity = new DateEntity(year, month, i, j % 7);
            if (year == mCurrentYear && month == mCurrentMonth && i == mCurrentDate) {
                dateEntity.isSelected = true;
            }

            if (year == mTodayYear && month == mTodayMonth && i == mTodayDate) {
                dateEntity.isToday = true;
            }
            dateEntities.add(dateEntity);
            lastWeekDayOfCurrentMonth = j % 7;
        }

        int yearOfNextMonth = year;
        int nextMonth = month + 1;
        if (month == 12) {
            yearOfNextMonth = year + 1;
            nextMonth = 1;
        }

        for (int i = 1, j = lastWeekDayOfCurrentMonth + 1; i < 7; i++, j++) {
            if (j >= 7) {
                break;
            }

            DateEntity dateEntity = new DateEntity(yearOfNextMonth, nextMonth, i, j);
            dateEntity.isInCurrentMonth = false;
            dateEntities.add(dateEntity);
        }

        return dateEntities;
    }

    /**
     * Get number of days within the designated month
     *
     * @param year  The designated year to find
     * @param month The designated month to find
     * @return int number of days of the designated month
     */
    private int getMonthDays(int year, int month) {
        int amount = 0;
        if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
            if (month > 0 && month <= 12) {
                amount = leapYearMonthDay[month - 1];
            }
        } else {
            if (month > 0 && month <= 12) {
                amount = commonYearMonthDay[month - 1];
            }
        }
        return amount;
    }

    /**
     * Get what is the weekday of the first day of designated month and year.
     * Weekday is then saved as Integer value.
     *
     * @param year  The designated year to find
     * @param month The designated month to find
     * @return int weekDay to find
     */
    private int getWeekDay(int year, int month) {
        int weekDay;
        int goneYearDays = 0;
        int thisYearDays = 0;

        /* Calculate number of days from year 1990 */
        for (int i = 1990; i < year; i++) {// From 1990 onwards, January 1, 1990 was Monday.
            if ((i % 4 == 0 && (i % 100 != 0)) || (i % 400 == 0)) {
                goneYearDays = goneYearDays + 366;
            } else {
                goneYearDays = goneYearDays + 365;
            }
        }

        /* Calculate number of days from beginning of year to previous month */
        if ((year % 4 == 0 && (year % 100 != 0)) || (year % 400 == 0)) {
            for (int i = 0; i < month - 1; i++) {
                thisYearDays = thisYearDays + leapYearMonthDay[i];
            }
        } else {
            for (int i = 0; i < month - 1; i++) {
                thisYearDays = thisYearDays + commonYearMonthDay[i];
            }
        }
        weekDay = (goneYearDays + thisYearDays + 1) % 7;

        return weekDay;
    }

    /**
     * Flush dates in weekly view.
     *
     *
     * @param distance_x The distance to check left/right fling
     */
    private void flushWeeklyDate(float distance_x) {
        int days = getMonthDays(mCurrentYear, mCurrentMonth);
        if (distance_x < 0) {
            if (mCurrentDate + 7 > days) {
                if (mCurrentMonth + 1 > 12) {
                    mDateEntities = initWeeklyDateList(mCurrentYear + 1, 1, mCurrentDate + 7 - days);
                } else {
                    mDateEntities = initWeeklyDateList(mCurrentYear, mCurrentMonth + 1, mCurrentDate + 7 - days);
                }
            } else {
                mDateEntities = initWeeklyDateList(mCurrentYear, mCurrentMonth, mCurrentDate + 7);
            }
        } else {
            if (mCurrentDate - 7 < 1) {
                if (mCurrentMonth - 1 <= 0) {
                    int previousDays = getMonthDays(mCurrentYear - 1, 12);
                    mDateEntities = initWeeklyDateList(mCurrentYear - 1, 12, mCurrentDate - 7 + previousDays);
                } else {
                    int previousDays = getMonthDays(mCurrentYear, mCurrentMonth - 1);
                    mDateEntities = initWeeklyDateList(mCurrentYear, mCurrentMonth - 1, mCurrentDate - 7 + previousDays);
                }

            } else {
                mDateEntities = initWeeklyDateList(mCurrentYear, mCurrentMonth, mCurrentDate - 7);
            }
        }
    }

    /**
     * Flush dates in monthly view.
     *
     *
     * @param distance_x The distance to check left/right fling
     */
    private void flushMonthlyDate(float distance_x) {
        if (distance_x < 0) {// Fling right
            if (mCurrentMonth + 1 > 12) {
                mDateEntities = initDateList(mCurrentYear + 1, 1, 1);
            } else {
                mDateEntities = initDateList(mCurrentYear, mCurrentMonth + 1, 1);
            }
        } else {// Fling left
            if (mCurrentMonth - 1 <= 0) {
                mDateEntities = initDateList(mCurrentYear - 1, 12, 1);
            } else {
                mDateEntities = initDateList(mCurrentYear, mCurrentMonth - 1, 1);
            }
        }
    }

    /**
     * Flush dates either in weekly or monthly view.
     * View type is checked from SharedPreferences.
     *
     * @param distance_x The distance to check left/right fling
     */
    public void flushDate(float distance_x) {
        SharedPreferences sp = mContext.getSharedPreferences(MainActivity.PREF_FILE_NAME, Activity.MODE_PRIVATE);
        int calType = sp.getInt(MainActivity.PREF_CAL_TYPE, 0);

        if (calType == CAL_MONTHLY) {
            flushMonthlyDate(distance_x);
        } else if (calType == CAL_WEEKLY) {
            flushWeeklyDate(distance_x);
        }
    }
}