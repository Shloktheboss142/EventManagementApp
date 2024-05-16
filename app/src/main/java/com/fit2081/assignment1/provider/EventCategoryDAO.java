package com.fit2081.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Data Access Object for the EventCategory
@Dao
public interface EventCategoryDAO {

    @Query("select * from eventCategory")
    LiveData<List<EventCategory>> getAllEventCategories();

    @Query("select * from eventCategory where categoryId = :categoryId")
    List<EventCategory> checkIfCategoryIdExists(String categoryId);

    @Query("update eventCategory set eventCount = eventCount + 1 where categoryId = :categoryId")
    void incrementCategoryCount(String categoryId);

    @Query("update eventCategory set eventCount = eventCount - 1 where categoryId = :categoryId")
    void decrementCategoryCount(String categoryId);

    @Insert
    void addEventCategory(EventCategory eventCategory);

    @Query("delete from eventCategory")
    void deleteAllEventCategories();

}
