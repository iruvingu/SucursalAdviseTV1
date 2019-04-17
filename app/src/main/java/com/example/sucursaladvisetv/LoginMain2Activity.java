package com.example.sucursaladvisetv;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opentok.android.Session;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginMain2Activity extends AppCompatActivity {

    private static String LOG_TAG = LoginMain2Activity.class.getSimpleName();
    private static final int RC_SETTINGS = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private TextView codeTv;
    private String androidDeviceId;
    private Handler handler = new Handler();

    //Variables
    private int delay = 5000;

    //Runnable
    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                nextScreen();
            }finally {
                handler.postDelayed(this, delay);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main2);

        codeTv = findViewById(R.id.codeTv);

        FirebaseApp.initializeApp(getApplicationContext());

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v("Etiqueta de Id Divece", "Android ID: " + androidDeviceId);
        codeTv.setText(androidDeviceId);
        requestPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        nextScreen();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    //Permisos chat

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermissions(){
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perm)){

        }else{
             EasyPermissions.requestPermissions(this, "Esta aplicación requiere acceso a su Cámara y Micrófono", RC_SETTINGS, perm);
        }
    }

    private void nextScreen() {
        /*firebase
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId).child("status");
        tvCodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        if(dataSnapshot.getValue(Boolean.class)){
                            Intent intent = new Intent(LoginMain2Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(LoginMain2Activity.this, "No tienes acceso", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        Toast.makeText(LoginMain2Activity.this,"Error carga datos", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginMain2Activity.this, "No existen Datos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginMain2Activity.this, "No existen Datos", Toast.LENGTH_SHORT).show();
            }
        });*/

        //FireStore
        DocumentReference tvcodeStore = firebaseFirestore.collection("screens").document(androidDeviceId);
        tvcodeStore.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    String active = String.valueOf(document.get("active"));
                    if (document.exists()&& active == "true") {
                        Intent intent = new Intent(LoginMain2Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.v("No existe pantalla", "No existe la pantalla");
                    }
                }else {
                    Log.v("No existe pantalla", "No existe la pantalla");
                }
            }
        });
    }

}
