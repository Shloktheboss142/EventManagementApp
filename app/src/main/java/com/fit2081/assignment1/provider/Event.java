package com.fit2081.assignment1.provider;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

// Event entity class
@Entity(tableName = "events")
public class Event {

    // Primary key for the event
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    @NotNull
    private int id;

    @ColumnInfo(name = "eventId")
    private String eventId;

    @ColumnInfo(name = "eventName")
    private String eventName;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "ticketsAvailable")
    private String ticketsAvailable;

    @ColumnInfo(name = "isActive")
    private String isActive;

    // Constructor to create the event
    public Event(String eventId, String eventName, String categoryId, String ticketsAvailable, String isActive) {
        this.setEventId(eventId);
        this.setEventName(eventName);
        this.setCategoryId(categoryId);
        this.setTicketsAvailable(ticketsAvailable);
        this.setIsActive(isActive);
    }

    // Getters and setters for the event

    public int getId() {
        return id;
    }

    public void setId(@NotNull int id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(String ticketsAvailable) {
        this.ticketsAvailable = ticketsAvailable;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
