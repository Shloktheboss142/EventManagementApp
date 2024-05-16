package com.fit2081.assignment1.provider;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Database class to create the database
@Database(entities = {EventCategory.class, Event.class}, version = 1)
public abstract class EMADatabase extends RoomDatabase {

    public static final String EVENT_CATEGORY_DATABASE_NAME = "event_category_database";

    public abstract EventCategoryDAO eventCategoryDAO();

    public abstract EventDAO eventDAO();

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile EMADatabase INSTANCE;

    static EMADatabase getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (EMADatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EMADatabase.class, EVENT_CATEGORY_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

}
