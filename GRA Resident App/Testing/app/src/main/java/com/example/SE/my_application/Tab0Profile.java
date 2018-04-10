package com.example.SE.my_application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.Inet4Address;

import static android.app.Activity.RESULT_OK;

public class Tab0Profile extends Fragment {

    private static final int  CHOOSE_IMAGE = 101;
    Button profileButton;
    EditText userName;
    ImageView profileImage;
    TextView verifiedTextBox;
    ProgressBar reportProgressBar2;

    Uri uriProfileImage;
    String profileImageUrl;

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab0profile, container, false);
        profileButton = rootView.findViewById(R.id.profileButton);
        userName = rootView.findViewById(R.id.userName);
        profileImage = rootView.findViewById(R.id.profileImage);
        verifiedTextBox = rootView.findViewById(R.id.verifiedTextBox);
        reportProgressBar2 = rootView.findViewById(R.id.reportProgressBar2);

        mAuth = FirebaseAuth.getInstance();

        loadUserInformation();

        profileButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     saveUserInformation();
                 }
             }

        );

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

       return rootView;
    }



    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();
        String photoUrl, displayName;

        if(user!=null) {
            if(user.getPhotoUrl() != null) {
//                photoUrl = user.getPhotoUrl().toString();
                Glide.with(this).load(user.getPhotoUrl().toString())
                        .into(profileImage);

            }

            if(user.getDisplayName() != null) {
//                displayName = user.getDisplayName();
                userName.setText(user.getDisplayName());
            }
        }

        //The email verification can be added to it when the issue of late delivery is registered
//        if(user.isEmailVerified()) {
//            verifiedTextBox.setText("Email Verified");
//        }
//        else {
//            verifiedTextBox.setText("Email Not Verified(Click to Verify)");
//            verifiedTextBox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(getActivity().getApplicationContext(),"Verification Email Sent", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        }





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uriProfileImage);
                profileImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserInformation() {
        String sUserName = userName.getText().toString().trim();
        reportProgressBar2.setVisibility(View.VISIBLE);

        if(sUserName.isEmpty()) {
            userName.setError("Display Name field cannot be empty");
            userName.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(sUserName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build()
                    ;

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity().getApplicationContext(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                reportProgressBar2.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!=null) {
            profileImageRef.putFile(uriProfileImage)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity().getApplicationContext(),"Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();

                }
            })
              .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(getActivity().getApplicationContext(),"Image Could not be Uploaded Successfully", Toast.LENGTH_SHORT).show();

                  }
              });

        }
    }



    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),CHOOSE_IMAGE);
    }
}
