package com.example.shobhan.gra_admin;

public class Feedback {
    String feedbackId;
    String grievanceId;
    String feedback;

    public Feedback() {
    }

    public Feedback(String feedbackId, String grievanceId, String feedback) {
        this.feedbackId = feedbackId;
        this.grievanceId = grievanceId;
        this.feedback = feedback;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public String getGrievanceId() {
        return grievanceId;
    }

    public String getFeedback() {
        return feedback;
    }
}
