package com.fit2081.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Data Access Object for the EventCategory
@Dao
public interface EventDAO {

    @Query("select * from events")
    LiveData<List<Event>> getAllEventsLive();

    @Query("select * from events")
    List<Event> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Query("delete from events where eventId = :eventId")
    void deleteEvent(String eventId);

    @Query("delete from events")
    void deleteAllEvents();

}
