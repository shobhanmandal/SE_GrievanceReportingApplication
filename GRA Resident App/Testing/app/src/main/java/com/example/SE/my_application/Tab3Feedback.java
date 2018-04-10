package com.example.SE.my_application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tab3Feedback extends Fragment {

    Button fSubmit;
    EditText feedback;
    Spinner fGreivanceSubject;

    DatabaseReference databaseFeedback,databaseGrievances;
    List<String> grievanceSubject;
    List<String> grievanceId;
    ArrayAdapter<String> dataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3feedback, container, false);

        databaseFeedback = FirebaseDatabase.getInstance().getReference("feedback");
        databaseGrievances = FirebaseDatabase.getInstance().getReference("grievances");

        fSubmit = rootView.findViewById(R.id.fSubmit);
        feedback = rootView.findViewById(R.id.feedback);
        fGreivanceSubject = rootView.findViewById(R.id.fGreivanceSubject);

        grievanceSubject = new ArrayList<>();
        grievanceId = new ArrayList<>();

        fSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });

        databaseGrievances.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                grievanceSubject.clear();
                grievanceId.clear();

                for(DataSnapshot grievancesSnapshot:dataSnapshot.getChildren()) {
                    Grievance grievance = grievancesSnapshot.getValue(Grievance.class);
                    grievanceSubject.add(grievance.getGrievanceSubject());
                    grievanceId.add(grievance.getGrievanceId());
                }

                dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, grievanceSubject);
                fGreivanceSubject.setAdapter(dataAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return rootView;
    }

    public void saveInformation() {
        String sfeedback = feedback.getText().toString().trim();
        String sGrievanceSubject = fGreivanceSubject.getSelectedItem().toString();
        String sGrievanceId = "";

        int j = grievanceSubject.indexOf(sGrievanceSubject);
        sGrievanceId = grievanceId.get(j);

        String feedbackId = databaseFeedback.push().getKey();
        Feedback feedback1 = new Feedback(feedbackId,sGrievanceId,sfeedback);

        databaseFeedback.child(feedbackId).setValue(feedback1);

        Toast.makeText(getActivity().getApplicationContext(),"Feedback for Grievance Registered Successfully", Toast.LENGTH_SHORT).show();

        feedback.setText("");

    }
}
