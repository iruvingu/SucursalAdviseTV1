package com.example.sucursaladvisetv;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private String androidDeviceId;
    private Boolean statusTv;
    private Intent intentLogin;
    private DatabaseReference tvCodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        tvCode();
        long milis = new Date().getTime();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        long milis = new Date().getTime();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        long milis = new Date().getTime();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(false);
    }


    public void tvCode(){
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        for(DataSnapshot getStatus: dataSnapshot.getChildren()){
                            switch (getStatus.getKey()){
                                case "status":
                                    statusTv = getStatus.getValue(Boolean.class);
                                    Log.v("LADB", "Status: " + statusTv);
                                    break;
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Error carga datos", Toast.LENGTH_LONG).show();
                    }
                    intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
                    if (!statusTv){
                        startActivity(intentLogin);
                        finish();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "No existen Datos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "No existen Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
