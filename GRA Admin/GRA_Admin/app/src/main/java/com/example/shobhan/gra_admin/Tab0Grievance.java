package com.example.shobhan.gra_admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class Tab0Grievance extends Fragment {

    ListView listViewGrievances;

    DatabaseReference databaseGrievances;
    DatabaseReference databaseEvents;

    ArrayList<GrievanceEvent> grievanceEventList;
    ArrayList<Grievance> grievanceLst;
    ArrayList<Event> eventLst;

    Intent intent;
    String desc = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab0grievance, container, false);


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


                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if(event.getGrievanceId().equals(gre.getGrievanceId())) {
                        desc = "\n\n" + event.getDescription() + desc;
                    }
                }

                databaseGrievances.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot grievancesSnapshot:dataSnapshot.getChildren()) {
                            Grievance grievance = grievancesSnapshot.getValue(Grievance.class);
                            if(grievance.getGrievanceId().equalsIgnoreCase(gre.grievanceId)) {
//                                Toast.makeText(getActivity().getApplicationContext(),"This is:"+desc, Toast.LENGTH_LONG).show();
                                String subject = gre.getGrievanceSubject();
                                String type = gre.getGrievanceType();
                                String status = gre.getEventType();
                                String grievanceId = gre.getGrievanceId();
                                String location = grievance.getLocation();

                                String toSend = subject+"!@!"+type+"!@!"+status+"!@!"+desc+"!@!"+grievanceId+"!@!"+location;
                                intent.putExtra("Value",toSend);
                                getActivity().startActivity(intent);
                                break;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                        for(Grievance g:grievanceLst) {
                            int temp_count=0;
                            String type ="Open";
                            for(Event e:eventLst) {
                                if((g.getGrievanceId().equals(e.getGrievanceId()))&&e.getCount()>=temp_count) {
                                    temp_count = e.getCount();
                                    type = e.getEventType();
                                }
                            }
                            ge = new GrievanceEvent(g.getGrievanceId(),g.getGrievanceSubject(),g.getGrievanceType(),g.getGrievanceDescription(),g.getImageLocation(),type);
                            grievanceEventList.add(ge);

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



}
