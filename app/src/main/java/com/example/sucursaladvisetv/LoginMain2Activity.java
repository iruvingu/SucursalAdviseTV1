package com.example.sucursaladvisetv;

import android.content.Intent;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class LoginMain2Activity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private TextView codeTv;
    private Boolean statusTv;
    private String androidDeviceId;
    private Intent intentMain;
    private DatabaseReference tvCodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main2);

        codeTv = findViewById(R.id.codeTv);

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v("Etiqueta de Id Divece", "Android ID: " + androidDeviceId);
        codeTv.setText(androidDeviceId);
        nextScreen();

    }

    private void nextScreen() {
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        for (DataSnapshot getStatus: dataSnapshot.getChildren()){
                            switch (getStatus.getKey()){
                                case "status":
                                    statusTv = getStatus.getValue(Boolean.class);
                                    break;
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(LoginMain2Activity.this,"Error carga datos", Toast.LENGTH_LONG).show();
                    }
                    intentMain = new Intent(getApplicationContext(),MainActivity.class);
                    if (statusTv){
                        startActivity(intentMain);
                    }else{
                        Toast.makeText(LoginMain2Activity.this, "No Tienes Acceso", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginMain2Activity.this, "No existen Datos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginMain2Activity.this, "No existen Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
