package com.example.SE.my_application;

public class Event {
    String eventId;
    String grievanceId;
    String description;
    String eventType;
    int count;

    public Event() {
    }

    public Event(String eventId,String grievanceId, String description, String eventType, int count) {
        this.eventId = eventId;
        this.grievanceId = grievanceId;
        this.description = description;
        this.eventType = eventType;
        this.count = count;
    }
    public String getEventId() {
        return eventId;
    }

    public String getGrievanceId() {
        return grievanceId;
    }

    public String getDescription() {
        return description;
    }

    public String getEventType() {
        return eventType;
    }

    public int getCount() {
        return count;
    }
}
