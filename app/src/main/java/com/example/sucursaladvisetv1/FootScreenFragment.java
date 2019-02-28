package com.example.sucursaladvisetv1;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootScreenFragment extends Fragment {


    TextView textView ;

    public FootScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_foot_screen, container, false);
        textView = root.findViewById(R.id.label_marquee);
        DatabaseReference myDataBaseRef = FirebaseDatabase
                .getInstance().getReference().child("anuncios");

        try{
            // Read from the database
            myDataBaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = "";
                    for (DataSnapshot string : dataSnapshot.getChildren()){
                        value += "·" + string.getValue(String.class) + "·" + "                         ";

                    }

                    //Marquee
                    textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    textView.setText(value);
                    textView.setSelected(true);
                    textView.setSingleLine();

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Problemas con el servidor", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

}
