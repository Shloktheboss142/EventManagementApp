package com.fit2081.assignment1;

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

    public void incrementCount() {
        String newCount = Integer.toString(Integer.parseInt(this.eventCount) + 1);
        this.eventCount = newCount;
    }

    public void decrementCount() {
        String newCount = Integer.toString(Integer.parseInt(this.eventCount) - 1);
        this.eventCount = newCount;
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
