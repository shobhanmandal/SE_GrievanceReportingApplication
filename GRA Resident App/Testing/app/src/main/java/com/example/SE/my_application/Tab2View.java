package com.example.SE.my_application;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tab2View extends Fragment{

    ListView listViewGrievances;

    DatabaseReference databaseGrievances;
    DatabaseReference databaseEvents;

    ArrayList<GrievanceEvent> grievanceEventList;
    ArrayList<Grievance> grievanceLst;
    ArrayList<Event> eventLst;

    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2view, container, false);

        databaseGrievances = FirebaseDatabase.getInstance().getReference("grievances");
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        listViewGrievances = rootView.findViewById(R.id.listViewGrievances);
        grievanceEventList = new ArrayList<>();
        grievanceLst = new ArrayList<>();
        eventLst = new ArrayList<>();

        loadTheData();
        listViewGrievances.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(),GrievanceDetails.class);
                GrievanceEvent gre = (GrievanceEvent)listViewGrievances.getItemAtPosition(position);
//                Toast.makeText(getActivity().getApplicationContext(),"This is:"+gre.getGrievanceSubject(), Toast.LENGTH_LONG).show();
                callToIntent(gre);
//                intent.putExtra("Number",gre);
//                getActivity().startActivity(intent);
            }
        });

//        loadTheData2();
//        GrievanceList adapter = new GrievanceList(getActivity(),grievanceEventList);
//        listViewGrievances.setAdapter(adapter);

        return rootView;
    }

    private void callToIntent(GrievanceEvent gre1){
        final GrievanceEvent gre = gre1;

        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String desc = "";
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if(event.getGrievanceId().equals(gre.getGrievanceId())) {
                        desc = "\n\n" + event.getDescription() + desc;
                    }
                }
//                Toast.makeText(getActivity().getApplicationContext(),"This is:"+desc, Toast.LENGTH_LONG).show();
                String subject = gre.getGrievanceSubject();
                String type = gre.getGrievanceType();
                String status = gre.getEventType();

                String toSend = subject+"!@!"+type+"!@!"+status+"!@!"+desc;
                intent.putExtra("Value",toSend);
                getActivity().startActivity(intent);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadTheData2() {
        GrievanceEvent grievanceEvent = new GrievanceEvent("123456","Testing","General","TEsting testing","No image Present","Open");
        grievanceEventList.add(grievanceEvent);

        grievanceEvent = new GrievanceEvent("123456","Testing1","General","TEsting testing1","No image Present","Open");
        grievanceEventList.add(grievanceEvent);

        grievanceEvent = new GrievanceEvent("123456","Testing2","General","TEsting testing2","No image Present","Open");
        grievanceEventList.add(grievanceEvent);
    }

    private void loadTheData() {
        databaseGrievances.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot grievancesSnapshot:dataSnapshot.getChildren()) {
                     Grievance grievance = grievancesSnapshot.getValue(Grievance.class);
//                    GrievanceEvent ge = new GrievanceEvent(grievance.getGrievanceSubject(),grievance.getGrievanceType(),grievance.getGrievanceDescription(),grievance.getImageLocation());
                    grievanceLst.add(grievance);
                }

                databaseEvents.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            grievanceEventList.clear();
                            for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                                Event event = eventSnapshot.getValue(Event.class);
                                eventLst.add(event);

                            }
                            GrievanceEvent ge;
//                            Log.i("*******My Outputs(UID):",FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());
                            for(Grievance g:grievanceLst) {
                                int temp_count=0;
                                String type ="Open";
//                                Log.i("+++++My Outputs(GID):",g.getUserId());
//                                Log.i("-----My Outputs(UID):",FirebaseAuth.getInstance().getCurrentUser().toString());
                                if(g.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())){
                                    for(Event e:eventLst) {
                                        if((g.getGrievanceId().equals(e.getGrievanceId()))&&e.getCount()>=temp_count) {
                                            temp_count = e.getCount();
                                            type = e.getEventType();
                                        }
                                    }
                                    ge = new GrievanceEvent(g.getGrievanceId(),g.getGrievanceSubject(),g.getGrievanceType(),g.getGrievanceDescription(),g.getImageLocation(),type);
                                    grievanceEventList.add(ge);
                                }


                            }

                            GrievanceList adapter = new GrievanceList(getActivity(),grievanceEventList);
                            listViewGrievances.setAdapter(adapter);

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

//    private void loadTheData() {
//        databaseGrievances.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }


}
