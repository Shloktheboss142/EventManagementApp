package com.fit2081.assignment1.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

// Event class entity class
@Entity(tableName = "eventCategory")
public class EventCategory {

    // Primary key for the event category
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    @NotNull
    private int id;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @ColumnInfo(name = "eventCount")
    private String eventCount;

    @ColumnInfo(name = "isActive")
    private String isActive;

    @ColumnInfo(name = "eventLocation")
    private String eventLocation;

    // Constructor to create the event category
    public EventCategory(String categoryId, String categoryName, String eventCount, String isActive, String eventLocation) {
        this.setCategoryId(categoryId);
        this.setCategoryName(categoryName);
        this.setEventCount(eventCount);
        this.setIsActive(isActive);
        this.setEventLocation(eventLocation);
    }

    // Getters and setters for the event category

    public int getId() {
        return id;
    }

    public void setId(@NotNull int id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEventCount() {
        return eventCount;
    }

    public void setEventCount(String eventCount) {
        this.eventCount = eventCount;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    // Method to increment the event count when a new event is added
    public void incrementCount() {
        this.eventCount = Integer.toString(Integer.parseInt(this.eventCount) + 1);
    }

    // Method to decrement the event count when a new event is added
    public void decrementCount() {
        this.eventCount = Integer.toString(Integer.parseInt(this.eventCount) - 1);
    }

}
