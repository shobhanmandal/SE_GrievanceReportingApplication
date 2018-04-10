package com.example.shobhan.gra_admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GrievanceDetails extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {
    TextView detailSubject, detailType,  detailDescription;

    MapView mMapView;
    private GoogleMap mGoogleMapp;
    Spinner grievanceType;
    Button grievanceSubmit;
    EditText grievanceUpdates;
    MarkerOptions marker;

    DatabaseReference databaseEvents;

    String array [] ={"Open","Resolved","Closed","Rejected"};
    String grievanceId;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

        detailSubject = findViewById(R.id.detailSubject);
        detailType = findViewById(R.id.detailType);
//        detailStatus = findViewById(R.id.detailStatus);
        detailDescription = findViewById(R.id.detailDescription);
        detailDescription.setMovementMethod(new ScrollingMovementMethod());

        grievanceType = findViewById(R.id.spinner1);
        grievanceSubmit = findViewById(R.id.grievanceSubmit);
        grievanceUpdates = findViewById(R.id.newComments);

        mMapView = findViewById(R.id.mapView);



        Bundle bundle = getIntent().getExtras();
        String received = "";
        if(bundle!=null) {
            received = bundle.getString("Value");
        }

//        Toast.makeText(this,"This is:"+received, Toast.LENGTH_LONG).show();
//        Log.i("+++++++++++My Content","+++this is received:"+received);

        String arr[] = received.split("!@!");
//        Log.i("+++++++++++My Content","+++this is after split:"+arr.length+", The contents");
//        for(String str:arr)
//            Log.i("+++++++++++My Content",str);
////        Toast.makeText(this,"This is:"+arr[0]+"||"+arr[1]+"||"+arr[2]+"||"+arr[3], Toast.LENGTH_LONG).show();

        detailSubject.setText(arr[0]);
        detailType.setText(arr[1]);
        int i=0;
        for(;i<4;i++) {
            if(array[i].equalsIgnoreCase(arr[2])) {
                break;
            }
        }
        grievanceType.setSelection(i);
        detailDescription.setText(arr[3]);
        grievanceId = arr[4];
        location = arr[5];
//        Log.i("+++++++++++My Content","+++this is after split:"+location);

        mMapView = findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


        grievanceSubmit.setOnClickListener(this);

    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstaceState) {
//        super.onViewCreated(view, savedInstaceState);
//
//        mMapView = findViewById(R.id.mapView);
//        if (mMapView != null) {
//            mMapView.onCreate(null);
//            mMapView.onResume();
//            mMapView.getMapAsync(this);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main__tabbed_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuLogout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(GrievanceDetails.this, MainActivity.class));
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.grievanceSubmit:
                grievanceUpdate();
                break;
        }
    }

    public void grievanceUpdate() {
        String description = grievanceUpdates.getText().toString().trim();
        String sGrievanceType = grievanceType.getSelectedItem().toString();
        int i=0;
        for(;i<4;i++) {
            if(array[i].equalsIgnoreCase(sGrievanceType)) {
                break;
            }
        }
        int count = i;
        if(description.isEmpty()) {
            grievanceUpdates.setError("Description Field cannot be empty");
            grievanceUpdates.requestFocus();
            return;
        }

        String eventId = databaseEvents.push().getKey();
        Event event = new Event(eventId,grievanceId,description,sGrievanceType,count);
        databaseEvents.child(eventId).setValue(event);
        grievanceUpdates.setText("");
        finish();
        Toast.makeText(this,"Update Made Successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(GrievanceDetails.this, Main_Tabbed_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);

        mGoogleMapp = googleMap;
        mGoogleMapp.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        String latlng [] = location.split(";");

        float lat = Float.parseFloat(latlng[0]);
        float lng = Float.parseFloat(latlng[1]);

        marker = new MarkerOptions().position(new LatLng(lat,lng));
        mGoogleMapp.addMarker(marker);

        CameraPosition markerCamera = CameraPosition.builder().target(new LatLng(lat,lng)).zoom(16).build();

        mGoogleMapp.moveCamera(CameraUpdateFactory.newCameraPosition(markerCamera));
    }
}
