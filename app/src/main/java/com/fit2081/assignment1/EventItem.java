package com.fit2081.assignment1;

// Object for an event
public class EventItem {

    private String eventId;
    private String eventName;
    private String categoryId;
    private String ticketsAvailable;
    private String isActive;

    public EventItem(String eventId, String eventName, String categoryId, String ticketsAvailable, String isActive) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.categoryId = categoryId;
        this.ticketsAvailable = ticketsAvailable;
        this.isActive = isActive;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getTicketsAvailable() {
        return ticketsAvailable;
    }

    public String getIsActive() {
        return isActive;
    }
}
