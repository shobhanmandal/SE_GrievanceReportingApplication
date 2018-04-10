package com.example.shobhan.gra_admin;

public class Grievance {
    String grievanceId;
    String userId;
    String grievanceSubject;
    String grievanceType;
    String grievanceDescription;
    String imageLocation;
    String location;

    public Grievance() {

    }

    public Grievance(String grievanceId, String userId, String grievanceSubject, String grievanceType, String grievanceDescription, String imageLocation, String location) {
        this.grievanceId = grievanceId;
        this.userId = userId;
        this.grievanceSubject = grievanceSubject;
        this.grievanceType = grievanceType;
        this.grievanceDescription = grievanceDescription;
        this.imageLocation = imageLocation;
        this.location = location;
    }

    public String getGrievanceId() {
        return grievanceId;
    }

    public String getUserId() {
        return userId;
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

    public String getLocation() {
        return location;
    }
}
