package com.example.sucursaladvisetv1;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    ViewPager viewPager;
    CustomSwipeAadapter adapter;
    private Context context;
    private String videoURL;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public MainCentralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        viewPager = (ViewPager) root.findViewById(R.id.view_pager);
        VideoView vidView = (VideoView)root.findViewById(R.id.myVideo);

        //Datos Firebase
        try{
            DatabaseReference VideoRef = database.getReference("AppMedia/videos/video1/url");

            VideoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    videoURL = (String) dataSnapshot.getValue();
                    Log.e("VALUE", "" + videoURL);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("DatabaseError","No hay datos en la rama" + databaseError.getCode());
                }
            });
        }catch (Exception e){
            Toast.makeText(context, "Video no visible", Toast.LENGTH_SHORT).show();
        }

        viewPager.setVisibility(View.GONE);
        adapter = new CustomSwipeAadapter(getActivity());
        viewPager.setAdapter(adapter);
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 2000, 4000);

        //Direccion remota de dominio publico aqui tendra q ir nuestra direccion de firebase
        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUri = Uri.parse(vidAddress);

        vidView.setVideoURI(vidUri);
        vidView.start();

        return root;
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem()==0){
                        viewPager.setCurrentItem(1);
                        //viewPager.setCurrentItem(1, true);
                    }else if(viewPager.getCurrentItem()==1){
                        viewPager.setCurrentItem(2);
                        //viewPager.setCurrentItem(2, true);
                    }else if(viewPager.getCurrentItem()==2){
                        viewPager.setCurrentItem(0);
                        //viewPager.setCurrentItem(0, true);
                    }
                }
            });
        }
    }

}
