package com.fit2081.assignment1;

// Object for an event category
public class EventCategoryItem {

    private String categoryId;
    private String categoryName;
    private String eventCount;
    private String isActive;

    public EventCategoryItem(String categoryId, String categoryName, String eventCount, String isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.eventCount = eventCount;
        this.isActive = isActive;
    }

    // Method to increment the event count when a new event is added
    public void incrementCount() {
        this.eventCount = Integer.toString(Integer.parseInt(this.eventCount) + 1);
    }

    // Method to decrement the event count when a new event is added
    public void decrementCount() {
        this.eventCount = Integer.toString(Integer.parseInt(this.eventCount) - 1);
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getEventCount() {
        return eventCount;
    }

    public String getIsActive() {
        return isActive;
    }

}
