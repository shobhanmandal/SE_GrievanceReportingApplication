package com.example.SE.my_application;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class Tab1Report extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    MapView mMapView;
    private GoogleMap mGoogleMapp;
    View mView;

    Button grievanceSubmit,clearOut;//,captureImageButton;
    EditText grievanceSubject, grievanceDetails;
    ImageView cameraImage;
    Spinner grievanceType;
    Uri uriGrievanceImage;
    ProgressBar reportProgressBar;

    private  static  final int REQUEST_CAMERA_IMAGE=123;
    private  static  final int REQUEST_PERMISSION=191;
    FirebaseAuth mAuth;
    MarkerOptions marker;
    String grievanceImageURL;

     DatabaseReference databaseGrievances;
     DatabaseReference databaseEvents;


    boolean checkImageTaken = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab1report, container, false);

        mAuth = FirebaseAuth.getInstance();
        databaseGrievances = FirebaseDatabase.getInstance().getReference("grievances");
        databaseEvents = FirebaseDatabase.getInstance().getReference("events");

//        captureImageButton = mView.findViewById(R.id.captureImageButton);
        grievanceSubmit = mView.findViewById(R.id.grievanceSubmit);
        clearOut = mView.findViewById(R.id.clearOut);

        cameraImage = mView.findViewById(R.id.cameraImage);

        grievanceSubject = mView.findViewById(R.id.grievanceSubject);
        grievanceDetails = mView.findViewById(R.id.grievanceDetails);
        grievanceType = mView.findViewById(R.id.grievancespinner);

        reportProgressBar = mView.findViewById(R.id.reportProgressBar);

//        captureImageButton.setOnClickListener(this);
        grievanceSubmit.setOnClickListener(this);
        clearOut.setOnClickListener(this);
        cameraImage.setOnClickListener(this);


//        //--------Filling up the list of grievances
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
//                R.array.grievancespinner, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstaceState) {
        super.onViewCreated(view, savedInstaceState);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMapp = googleMap;
        mGoogleMapp.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        marker = new MarkerOptions().position(new LatLng(17.445236, 78.349758)).draggable(true);
        mGoogleMapp.addMarker(marker);

        CameraPosition markerCamera = CameraPosition.builder().target(new LatLng(17.445236, 78.349758)).zoom(16).build();

        mGoogleMapp.moveCamera(CameraUpdateFactory.newCameraPosition(markerCamera));
    }

    private void grievanceRegister() {
        String sGrievanceSubject = grievanceSubject.getText().toString().trim();
        String sGrievanceDetails = grievanceDetails.getText().toString().trim();
        String sGrievanceType = grievanceType.getSelectedItem().toString();
        String position = marker.getPosition().latitude+";"+marker.getPosition().longitude;
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("grievanceImages/"+mAuth.getCurrentUser().toString()+System.currentTimeMillis()+".jpg");

        if(sGrievanceSubject.isEmpty()) {
            grievanceSubject.setError("Subject of the grievance is required");
            grievanceSubject.requestFocus();
            return;
        }

        if(sGrievanceDetails.isEmpty()) {
            grievanceDetails.setError("Details regarding the grievance is required");
            grievanceDetails.requestFocus();
            return;
        }

        reportProgressBar.setVisibility(View.VISIBLE);
        if(uriGrievanceImage==null) {
            Toast.makeText(getActivity().getApplicationContext(),"No image is being selected", Toast.LENGTH_LONG).show();
            grievanceImageURL = "No image Present";

            String grievanceId = databaseGrievances.push().getKey();
            String eventId = databaseEvents.push().getKey();

            Grievance grievance = new Grievance(grievanceId,
                    mAuth.getCurrentUser().getEmail().toString(),
                    grievanceSubject.getText().toString().trim(),
                    grievanceType.getSelectedItem().toString(),
                    grievanceDetails.getText().toString().trim(),
                    grievanceImageURL,
                    marker.getPosition().latitude+";"+marker.getPosition().longitude);
            Event event = new Event(eventId,grievanceId,grievanceDetails.getText().toString().trim(),"Open",0);

            databaseGrievances.child(grievanceId).setValue(grievance);
            databaseEvents.child(eventId).setValue(event);

//            databaseEvents.
//            databaseEvents.child("L9Q4QXuXLwXw1vYjlLQ").child("eventType").setValue("Closed");

            Toast.makeText(getActivity().getApplicationContext(),"Grievance Registered Successfully", Toast.LENGTH_SHORT).show();
            reportProgressBar.setVisibility(View.GONE);
            clearAllInput();

        }
        else {


            profileImageRef.putFile(uriGrievanceImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            grievanceImageURL = taskSnapshot.getDownloadUrl().toString();
                            String grievanceId = databaseGrievances.push().getKey();
                            String eventId = databaseEvents.push().getKey();

                            Grievance grievance = new Grievance(grievanceId,
                                                                mAuth.getCurrentUser().toString(),
                                                                grievanceSubject.getText().toString().trim(),
                                                                grievanceType.getSelectedItem().toString(),
                                                                grievanceDetails.getText().toString().trim(),
                                                                grievanceImageURL,
                                                                marker.getPosition().latitude+";"+marker.getPosition().longitude);
                            Event event = new Event(eventId,grievanceId,grievanceDetails.getText().toString().trim(),"Open",0);

                            databaseGrievances.child(grievanceId).setValue(grievance);
                            databaseEvents.child(eventId).setValue(event);

                            Toast.makeText(getActivity().getApplicationContext(),"Grievance Registered Successfully", Toast.LENGTH_SHORT).show();
                            reportProgressBar.setVisibility(View.GONE);
                            clearAllInput();

                        }
                    });

        }


    }




    private void takeImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "Grievance.jpg");
//        Uri uri = FileProvider.getUriForFile(getContext(),"com.examples.SE.my_application"+)
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//        takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE);


//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager())!=null){
//            startActivityForResult(takePictureIntent,CAM_REQUEST);
//        }
    }

    private void clearAllInput() {
        grievanceSubject.setText("");
        grievanceDetails.setText("");
        cameraImage.setImageBitmap(null);
        int id = R.drawable.grievanceimage;
        cameraImage.setImageResource(id);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cameraImage:
                showImageChooser();
                break;

            case R.id.grievanceSubmit:
                grievanceRegister();
                break;

            case R.id.clearOut:
                clearAllInput();
                break;

        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//
//        Toast.makeText(getActivity().getApplicationContext(),"It came here", Toast.LENGTH_LONG).show();
//        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK ) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            cameraImage.setImageBitmap(imageBitmap);
//
////            File file = new File(Environment.getExternalStorageDirectory(), "Grievance.jpg");
////            uriGrievanceImage =FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".provider", file);
//
//
//
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageBitmap, "Title", null);
//            Toast.makeText(getActivity().getApplicationContext(),path, Toast.LENGTH_SHORT).show();
////            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), imageBitmap, "Title", null);
//            uriGrievanceImage = Uri.parse(path);
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            uriGrievanceImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uriGrievanceImage);
                cameraImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Grievance Image"),REQUEST_CAMERA_IMAGE);
    }



}




//    public Uri getImageUri(Context inContext, Bitmap inImage) {
////        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//public class Tab1Report extends Fragment implements OnMapReadyCallback{
//
//    MapView mMapView;
//    private GoogleMap googleMap;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.tab1report, container, false);
//        mMapView = (MapView) rootView.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
//
//        mMapView.onResume();
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);
//
//                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });
//
//        return rootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }
//}
