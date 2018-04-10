package com.example.shobhan.gra_admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextPassword, editTextEmailId;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPassword = findViewById(R.id.password);
        editTextEmailId = findViewById(R.id.emailId);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.login).setOnClickListener(this);
    }

    private void loginActivity() {
        String password = editTextPassword.getText().toString().trim();
        String emailId = editTextEmailId.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        if(!emailId.equalsIgnoreCase("admin@gmail.com")) {
            editTextPassword.setError("This is not admin email");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailId,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(MainActivity.this, Main_Tabbed_Activity.class);
//                            Toast.makeText(getApplicationContext(), "Well we will be loggin soon", Toast.LENGTH_LONG).show();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this, Main_Tabbed_Activity.class));
//            Toast.makeText(getApplicationContext(), "Well we will be loggin soon", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.login:
                loginActivity();
                break;
        }
    }
}
