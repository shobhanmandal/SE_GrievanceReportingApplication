package com.example.shobhan.gra_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tab1Feedback extends Fragment {

    ListView listViewFeedback;

    DatabaseReference databaseGrievances;
    DatabaseReference databaseFeedback;

    ArrayList<Grievance> grievanceLst;
    ArrayList<Feedback> feedbackLst;
    ArrayList<Grievance> trimmedGrievanceList;

    Intent intent;
    String desc = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1feedback, container, false);


        databaseGrievances = FirebaseDatabase.getInstance().getReference("grievances");
        databaseFeedback = FirebaseDatabase.getInstance().getReference("feedback");

        listViewFeedback = rootView.findViewById(R.id.listViewFeedback);
        grievanceLst = new ArrayList<>();
        feedbackLst = new ArrayList<>();
        trimmedGrievanceList = new ArrayList<>();

        loadTheData();
        listViewFeedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(),FeedbackDetails.class);
                Grievance gre = (Grievance)listViewFeedback.getItemAtPosition(position);
//                Toast.makeText(getActivity().getApplicationContext(),"This is:"+gre.getGrievanceSubject(), Toast.LENGTH_LONG).show();
                callToIntent(gre);
//                intent.putExtra("Number",gre);
//                getActivity().startActivity(intent);
            }
        });
        return rootView;
    }

    private void callToIntent(Grievance gre1){
        final Grievance gre = gre1;
        databaseFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String feedbackDesc="";
                for(DataSnapshot feedbackSnapshot:dataSnapshot.getChildren()) {
                    Feedback fb = feedbackSnapshot.getValue(Feedback.class);
                    if(gre.getGrievanceId().equalsIgnoreCase(fb.getGrievanceId())) {
                        feedbackDesc = "\n"+fb.getFeedback() +feedbackDesc;
                    }
                 }

                String subject = gre.getGrievanceSubject();
                String description = gre.getGrievanceDescription();
                String toSend = subject+"!@!"+description+"!@!"+feedbackDesc;
                intent.putExtra("FeedbackValue",toSend);
                getActivity().startActivity(intent);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void loadTheData() {
        databaseFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot feedbackSnapshot:dataSnapshot.getChildren()) {
                    Feedback fb = feedbackSnapshot.getValue(Feedback.class);
                    feedbackLst.add(fb);
                }

                databaseGrievances.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        trimmedGrievanceList.clear();
                        for(DataSnapshot grievancesSnapshot: dataSnapshot.getChildren()) {
                            Grievance grievance = grievancesSnapshot.getValue(Grievance.class);
                            grievanceLst.add(grievance);

                        }

                        for(Feedback fe:feedbackLst) {
                            for(Grievance ge:grievanceLst) {
                                if(ge.getGrievanceId().equalsIgnoreCase(fe.getGrievanceId()))
                                {
                                    trimmedGrievanceList.add(ge);

                                }
                            }
                        }

                        FeedbackList adapter1=new FeedbackList(getActivity(),trimmedGrievanceList);
                        listViewFeedback.setAdapter(adapter1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
