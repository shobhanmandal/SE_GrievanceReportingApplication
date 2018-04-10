package com.example.shobhan.gra_admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackDetails extends AppCompatActivity {

    TextView feedbackSubject, feedbackDescription,  feedbackFeedback;

    DatabaseReference databaseFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        databaseFeedback = FirebaseDatabase.getInstance().getReference("feedback");

        feedbackSubject = findViewById(R.id.feedbackSubject);
        feedbackDescription = findViewById(R.id.feedbackDescription);
        feedbackFeedback = findViewById(R.id.feedbackFeedback);

        Bundle bundle = getIntent().getExtras();
        String received = bundle.getString("FeedbackValue");

        String arr[] = received.split("!@!");

        feedbackSubject.setText(arr[0]);
        feedbackDescription.setText(arr[1]);
        feedbackFeedback.setText(arr[2]);

    }
}
