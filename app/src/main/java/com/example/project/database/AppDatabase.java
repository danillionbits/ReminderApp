package com.example.project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Database entity for Event class
 * This class informs android studio that a pre-populated database is added
 *
 * @author Dat Pham, Tran Cong Minh, Santosh Nyaupane
 * @version 0.0.1
 * @since 2022-03-01
 */
@Database(entities = {Event.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();

    private static final String DB_NAME = "app_database.db";
    private static AppDatabase instance;

    /**
     * Gets instance of singleton AppDatabase. If instance does
     * not exist, create() function is called.
     *
     * @param context Context
     * @return instance of AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (null == instance) {
            instance = create(context);
        }
        return instance;
    }

    /**
     * Create an instance of AppDatabase.
     *
     * @param context Context
     * @return instance of AppDatabase
     */
    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                // normally you should run in background, avoid to freeze UI and get killed by OS
                .allowMainThreadQueries()
                .build();
    }
}
