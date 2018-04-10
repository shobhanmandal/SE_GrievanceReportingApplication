package com.example.SE.my_application;

import java.io.Serializable;

public class GrievanceEvent {
    String grievanceId;
    String grievanceSubject;
    String grievanceType;
    String grievanceDescription;
    String imageLocation;
    String eventType;

    public GrievanceEvent() {
    }

    public GrievanceEvent(String grievanceId,String grievanceSubject, String grievanceType, String grievanceDescription, String imageLocation, String eventType) {
        this.grievanceId = grievanceId;
        this.grievanceSubject = grievanceSubject;
        this.grievanceType = grievanceType;
        this.grievanceDescription = grievanceDescription;
        this.imageLocation = imageLocation;
        this.eventType = eventType;
    }

    public String getGrievanceId() {
        return grievanceId;
    }

    public String getGrievanceSubject() {
        return grievanceSubject;
    }

    public String getGrievanceType() {
        return grievanceType;
    }

    public String getGrievanceDescription() {
        return grievanceDescription;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getEventType() {
        return eventType;
    }
}
