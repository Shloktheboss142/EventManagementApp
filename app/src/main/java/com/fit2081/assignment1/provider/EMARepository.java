package com.fit2081.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

// Repository class to handle the data
public class EMARepository {

    private EventCategoryDAO mEventCategoryDAO;
    private LiveData<List<EventCategory>> mAllEventCategories;
    private EventDAO mEventDAO;
    private LiveData<List<Event>> mAllEvents;


    // Constructor to create the database and get the data
    public EMARepository(Application application) {
        EMADatabase db = EMADatabase.getDatabase(application);
        mEventCategoryDAO = db.eventCategoryDAO();
        mAllEventCategories = mEventCategoryDAO.getAllEventCategories();
        mEventDAO = db.eventDAO();
        mAllEvents = mEventDAO.getAllEventsLive();
    }

    // Methods to get the specific data

    LiveData<List<EventCategory>> getAllEventCategories() {
        return mAllEventCategories;
    }

    LiveData<List<Event>> getAllEventsLive() {
        return mAllEvents;
    }

    List<Event> getAllEvents() {
        return mEventDAO.getAllEvents();
    }

    List<EventCategory> checkIfCategoryIdExists(String categoryId){
        return mEventCategoryDAO.checkIfCategoryIdExists(categoryId);
    }

    void insert(EventCategory eventCategory) {
        EMADatabase.databaseWriteExecutor.execute(() -> mEventCategoryDAO.addEventCategory(eventCategory));
    }

    void insert(Event event) {
        EMADatabase.databaseWriteExecutor.execute(() -> mEventDAO.addEvent(event));
    }

    void deleteAllCategories(){
        EMADatabase.databaseWriteExecutor.execute(()->mEventCategoryDAO.deleteAllEventCategories());
    }

    void deleteEvent(String eventId){
        EMADatabase.databaseWriteExecutor.execute(()->mEventDAO.deleteEvent(eventId));
    }

    void deleteAllEvents(){
        EMADatabase.databaseWriteExecutor.execute(()->mEventDAO.deleteAllEvents());
    }

    void incrementCategoryCount(String categoryId){
        EMADatabase.databaseWriteExecutor.execute(()->mEventCategoryDAO.incrementCategoryCount(categoryId));
    }

    void decrementCategoryCount(String categoryId){
        EMADatabase.databaseWriteExecutor.execute(()->mEventCategoryDAO.decrementCategoryCount(categoryId));
    }

}
