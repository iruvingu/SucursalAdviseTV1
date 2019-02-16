package com.example.sucursaladvisetv1;


import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadScreenFragment extends Fragment {

    private TextView dateTextView;

    //Calendario para obtener fecha & hora
    public final Date currentTime = Calendar.getInstance().getTime();

    public HeadScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_head_screen, container, false);

        dateTextView =  root.findViewById(R.id.dateView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
        String currentDateandTime = sdf.format(currentTime);
        dateTextView.setText(currentDateandTime);

        return root;
    }


}
