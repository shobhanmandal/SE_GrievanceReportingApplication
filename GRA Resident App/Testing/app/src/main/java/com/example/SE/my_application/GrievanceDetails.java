package com.example.SE.my_application;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class GrievanceDetails extends AppCompatActivity {

    TextView detailSubject, detailType, detailStatus, detailDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        detailSubject = findViewById(R.id.detailSubject);
        detailType = findViewById(R.id.detailType);
        detailStatus = findViewById(R.id.detailStatus);
        detailDescription = findViewById(R.id.detailDescription);
        detailDescription.setMovementMethod(new ScrollingMovementMethod());

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
        detailStatus.setText(arr[2]);
        detailDescription.setText(arr[3]);

    }

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

}

