package com.example.SE.my_application;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import static java.sql.DriverManager.println;

/*public class SignUpActivity extends AppCompatActivity {

    Button existingUser;
    Button signUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;

        existingUser = findViewById(R.id.existingUser);

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}*/


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText  editTextPassword, editTextConfirmPassword, editTextEmailId;
    ProgressBar rProgressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextPassword = findViewById(R.id.rPassword);
        editTextConfirmPassword = findViewById(R.id.rConfirmPassword);
        editTextEmailId = findViewById(R.id.rEmailId);
        rProgressBar = findViewById(R.id.rProgressBar);


        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.existingUser).setOnClickListener(this);
        findViewById(R.id.signUp).setOnClickListener(this);
    }

    private void registerUser() {


        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String emailId = editTextEmailId.getText().toString().trim();
//        System.out.print("It came here");


        if(emailId.isEmpty()) {
            editTextEmailId.setError("Email is required");
            editTextEmailId.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            editTextEmailId.setError("Email is not valid");
            editTextEmailId.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6) {
            editTextPassword.setError("Minimum length of Password is 6");
            editTextPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm Password is required");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Password do not match");
            editTextConfirmPassword.requestFocus();
            return;
        }

        rProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            rProgressBar.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            finish();
                            Toast.makeText(getApplicationContext(),"User Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, Main_Tabbed_Activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            rProgressBar.setVisibility(View.GONE);
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(),"User is already registered", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View v) {
        println("Id that is coming up:"+R.id.signUp);
        Log.i("Checking","This is coming up"+R.id.signUp);
        switch(v.getId()) {
            case R.id.existingUser:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.signUp:
                registerUser();
//                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}