package com.example.project.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Access and handle data from Event table using SQLite queries
 *
 * @author Dat Pham
 * @version 0.0.1
 * @since 2022-03-01
 */
@Dao
public interface EventDao {
    /**
     * Create a new event
     *
     * @param event Event
     */
    @Insert
    void insert(Event event);

    /**
     * Update an existing event
     *
     * @param event Event
     */
    @Update
    void update(Event event);

    /**
     * Delete an event based on eventId
     *
     * @param id long
     */
    @Query("DELETE FROM event WHERE eventId = :id")
    void deleteById(long id);

    /**
     * Get all events whose currentDate is equal or smaller than beginDate
     * or endDate. The table is ordered by beginDate.
     *
     * @param currentDate String
     * @return List of Event that meets above conditions
     */
    @Query("SELECT * FROM event WHERE DATE(:currentDate) <= DATE(beginDate) OR DATE(:currentDate) <= DATE(endDate) ORDER BY DATE(beginDate) ASC")
    List<Event> getAll(String currentDate);

    /**
     * Get an event based on eventId
     *
     * @param id long
     * @return event Event that has the same id
     */
    @Query("SELECT * FROM event WHERE eventId = :id")
    Event getById(long id);
}
