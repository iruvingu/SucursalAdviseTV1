package com.example.sucursaladvisetv;

import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentListenOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private String androidDeviceId;
    private Object refreshed;
    private ObjectScreen objectScreen;
    private ObjectScreen objectScreenNew;
    private Intent intentLogin;
    private DatabaseReference tvCodeRef;
    private Handler handler = new Handler();
    private long milis = new Date().getTime();
    private InterfaceRetrofit interfaceRetrofit = RetrofitClass.newInstance();

    //Variables
    /*private int delay = 5000;

    //Runnable
    final  Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                refreshed();
            }finally {
                handler.postDelayed(this,delay);
            }

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        tvCode();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(true);
        statusTrue();
        refreshed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshedListener();
        statusTrue();
        //handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onStop() {
        super.onStop();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(false);
        statusFalse();
        //handler.removeCallbacks(runnable);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId);
        tvCodeRef.child("statusAPP").child(String.valueOf(milis)).setValue(true);
        statusTrue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("LADB", "Im death");
        if (objectScreen.getRefreshed().compareTo(objectScreenNew.getRefreshed()) == -1){
            firebaseFirestore.collection("screens").document(androidDeviceId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    objectScreen = documentSnapshot.toObject(ObjectScreen.class);
                }
            });
        intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
        startActivity(intentLogin);
        }else{
            Log.v("LADB", "Else OnDestroy");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intentLogin = new Intent(getApplicationContext(),LoginMain2Activity.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    private void tvCode() {
            /*Firebase
            tvCodeRef = database.getReference().child("tv_codes/"+ androidDeviceId).child("status");
            tvCodeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        try{
                            if(!dataSnapshot.getValue(Boolean.class)){
                                intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
                                startActivity(intentLogin);
                                finish();
                            }
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"Error carga datos", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "No existen Datos", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "No existen Datos", Toast.LENGTH_SHORT).show();
                }
            });*/

            //FireStore
        DocumentReference tvcodeStore = firebaseFirestore.collection("screens").document(androidDeviceId);
        tvcodeStore.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("LADB", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    //Log.d("LADB", "Current data: " + documentSnapshot.getData());
                    //Log.d("LADB", "Current data1: " + documentSnapshot.get("active"));
                    String active = String.valueOf(documentSnapshot.get("active"));
                    if (active == "false") {
                        intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
                        startActivity(intentLogin);
                        finish();
                    }
                } else {
                    intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
                    startActivity(intentLogin);
                    finish();
                    Log.d("LADB", "Current data: null");
                }
            }
        });
    }


    private void refreshed() {
        DocumentReference tvcodeRefres = firebaseFirestore.collection("screens").document(androidDeviceId);
        tvcodeRefres.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        refreshed = document.get("refreshed");
                        //Log.v("LADB", "Refreshed: " + refreshed);
                    } else {
                        Log.v("No existe pantalla", "No existe la pantalla");
                    }
                }else{
                    Log.d("LADB", "Current data: null");
                }
            }
        });
    }

    private void refreshedListener(){
        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        firebaseFirestore.collection("screens").document(androidDeviceId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                objectScreen = documentSnapshot.toObject(ObjectScreen.class);
            }
        });

        firebaseFirestore.collection("screens").document(androidDeviceId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("LADB", "Listen failed.", e);
                    return;
                }

                if(documentSnapshot != null && documentSnapshot.exists()) {
                    objectScreenNew = documentSnapshot.toObject(ObjectScreen.class);
                    Log.v("LADB", "objectScreen: " + objectScreen);
                    Log.v("LADB", "objectScreenNEW: " + objectScreenNew);
                    try{
                        Log.v("LADB", "" + objectScreen.getRefreshed().compareTo(objectScreenNew.getRefreshed()));
                        if (objectScreen.getRefreshed().compareTo(objectScreenNew.getRefreshed()) == -1) {
                            Log.v("LADB", "" + objectScreen.getRefreshed().compareTo(objectScreenNew.getRefreshed()));
                            finish();
                        }else{
                            Log.v("LADB", "Good");
                        }
                    }catch (Exception e1){
                        Log.v("LADB", "Dato nulo");
                    }
                }else{
                        Log.v("LADB", "Sin Consulta");
                    }
            }
        });

        /*CollectionReference reference = firebaseFirestore.collection("screens");
        reference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( @Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("LADB", "Listen failed.", e);
                    return;
                }

                Log.v("LADB", "Query: "+ String.valueOf(queryDocumentSnapshots));
                for(DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                    DocumentSnapshot documentSnapshot = documentChange.getDocument();
                    switch (documentChange.getType()){
                        case ADDED:
                            Log.v("LADB", "ADD");
                            break;
                        case MODIFIED:
                            Log.v("LADB", "MODIFIED");
                            intentLogin = new Intent(getApplicationContext(), LoginMain2Activity.class);
                            startActivity(intentLogin);
                            finish();
                            break;
                        case REMOVED:
                            Log.v("LADB", "REMOVED");
                            break;

                    }
                }
            }
        });*/
    }

    //Servicio retrofit true
    private void statusTrue() {
        Call<RetrofitClass.Response> responseCall = interfaceRetrofit
                .getBeboteService(new RetrofitClass.Request(androidDeviceId, String.valueOf(milis),true));
        responseCall.enqueue(new Callback<RetrofitClass.Response>() {
            @Override
            public void onResponse(Call<RetrofitClass.Response> call, Response<RetrofitClass.Response> response) {
                switch (response.code()){
                    case 200:
                        //Toast.makeText(MainActivity.this, "Servicio: " + response.body().sucess, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<RetrofitClass.Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Conectar el cable de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Servicio Retrofit false
    private void statusFalse(){
        Call<RetrofitClass.Response> responseCall = interfaceRetrofit
                .getBeboteService(new RetrofitClass.Request(androidDeviceId, String.valueOf(milis),false));
        responseCall.enqueue(new Callback<RetrofitClass.Response>() {
            @Override
            public void onResponse(Call<RetrofitClass.Response> call, Response<RetrofitClass.Response> response) {
                switch (response.code()){
                    case 200:
                        //Toast.makeText(MainActivity.this, "Servicio: " + response.body().sucess, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<RetrofitClass.Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Conectar el cable de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
