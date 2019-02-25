package com.example.sucursaladvisetv1;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.DatePickerDialog;;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadScreenFragment extends Fragment {

    private TextView dateTextView, hourTv, sucursalNameTextView;

    // A class instance
    private Handler mHandler = new Handler(Looper.getMainLooper());

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public HeadScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_head_screen, container, false);

        dateTextView =  root.findViewById(R.id.dateView);
        hourTv = root.findViewById(R.id.hourView);
        sucursalNameTextView = root.findViewById(R.id.SucursalId);

        //Datos para fecha y hora
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Calendario para obtener fecha & hora
                                Date currentTime = Calendar.getInstance().getTime();
                                SimpleDateFormat date_sdf = new SimpleDateFormat("dd/MM/yyyy");
                                SimpleDateFormat hour_sdf = new SimpleDateFormat("HH:mm a");

                                String currentDate = date_sdf.format(currentTime);
                                String currentHour = hour_sdf.format(currentTime);

                                dateTextView.setText(currentDate);
                                hourTv.setText(currentHour);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.v("InterruptedException", e.getMessage());
                }
            }
        };

        thread.start();

        //Datos de Sucursal Firebase
        try {
            DatabaseReference SucursalRef =
                    database.getReference("Sucursales/sucursal_0/informacion/nombre");

            SucursalRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nombreSucursal = (String) dataSnapshot.getValue();
                    sucursalNameTextView.setText(getString(R.string.bienvenida) + " " + nombreSucursal);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DatabaseError","No hay datos en la rama" + databaseError.getCode());
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Sucursal Inexistente", Toast.LENGTH_SHORT).show();
        }


        return root;
    }


}
