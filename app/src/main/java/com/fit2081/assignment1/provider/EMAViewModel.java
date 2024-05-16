package com.fit2081.assignment1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// ViewModel class to handle the data
public class EMAViewModel extends AndroidViewModel {

    private EMARepository mRepository;
    private LiveData<List<EventCategory>> mAllEventCategories;
    private LiveData<List<Event>> mAllEvents;

    // Constructor to create the database and get the data
    public EMAViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EMARepository(application);
        mAllEventCategories = mRepository.getAllEventCategories();
        mAllEvents = mRepository.getAllEventsLive();
    }

    // Methods to get the specific data

    public LiveData<List<EventCategory>> getAllEventCategories() {
        return mAllEventCategories;
    }

    public LiveData<List<Event>> getAllEventsLive() {
        return mAllEvents;
    }

    public List<Event> getAllEvents() {
        return mRepository.getAllEvents();
    }

    public List<EventCategory> checkIfCategoryIdExists(String categoryId){
        return mRepository.checkIfCategoryIdExists(categoryId);
    }

    public void insert(EventCategory eventCategory) {
        mRepository.insert(eventCategory);
    }

    public void insert(Event event) {
        mRepository.insert(event);
    }

    public void deleteAllCategories(){
        mRepository.deleteAllCategories();
    }

    public void deleteEvent(String eventId){
        mRepository.deleteEvent(eventId);
    }

    public void deleteAllEvents(){
        mRepository.deleteAllEvents();
    }

    public void incrementCategoryCount(String categoryId){
        mRepository.incrementCategoryCount(categoryId);
    }

    public void decrementCategoryCount(String categoryId){
        mRepository.decrementCategoryCount(categoryId);
    }

}
