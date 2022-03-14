package com.example.project.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Define Event class for AppDatabase
 *
 * @author Dat Pham, Tran Cong Minh
 * @version 0.0.1
 * @since 2022-02-27
 */
@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long eventId;
    private String title;
    private String beginDate;
    private String beginTime;
    private String endDate;
    private String endTime;
    private String location;
    private String attendees;
    private String color;
    private String description;
    private static final String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    /**
     * Constructor for Event class
     *
     * @param title       assign title for event
     * @param beginDate   assign begin date for event
     * @param beginTime   assign begin time for event
     * @param endDate     assign end date for event
     * @param endTime     assign end time for event
     * @param location    assign location for event
     * @param attendees   assign attendees for event
     * @param color       assign color status for event
     * @param description assign additional description for event
     */
    public Event(String title, String beginDate, String beginTime, String endDate, String endTime, String location, String attendees, String color, String description) {
        this.title = title;
        this.beginDate = beginDate;
        this.beginTime = beginTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.location = location;
        this.attendees = attendees;
        this.color = color;
        this.description = description;
    }

    /**
     * Constructor for Event class with default value.
     * This constructor is mostly for creating new event
     */
    public Event() {
        this("", today, "08:00", today, "09:00", "", "", "#9C27B0", "");
    }

    /**
     * Get event's id
     *
     * @return long id of event
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * Set event's id
     *
     * @param eventId long
     */
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    /**
     * Get event's title
     *
     * @return String title of event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set event's title
     *
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get event's begin date
     *
     * @return String beginDate of event
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Set event's begin date
     *
     * @param beginDate String
     */
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * Get event's begin time
     *
     * @return String beginTime of event
     */
    public String getBeginTime() {
        return beginTime;
    }

    /**
     * Set event's beginTime
     *
     * @param beginTime String
     */
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * Get event's end date
     *
     * @return String endDate of event
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set event's end date
     *
     * @param endDate String
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Get event's end time
     *
     * @return String endTime of event
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Set event's end time
     *
     * @param endTime String
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    /**
     * Get event's location
     *
     * @return String location of event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set event's location
     *
     * @param location String
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get event's attendees
     *
     * @return String attendees of event
     */
    public String getAttendees() {
        return attendees;
    }

    /**
     * Set event's attendees
     *
     * @param attendees String
     */
    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    /**
     * Get event's color status
     *
     * @return String color of event
     */
    public String getColor() {
        return color;
    }

    /**
     * Set event's color status
     *
     * @param color String
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get event's additional description
     *
     * @return String additional description of event
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set event's additional description
     *
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get event's title by default
     *
     * @return title String
     */
    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
