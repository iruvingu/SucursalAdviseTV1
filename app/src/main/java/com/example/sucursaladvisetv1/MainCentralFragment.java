package com.example.sucursaladvisetv1;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    private ViewPager viewPager;
    private CustomSwipeAadapter adapter;
    private VideoView videoView;
    private Context context;
    private String videoURL;

    private Boolean isPlaying = false;

    //Firebase RTD
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public MainCentralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        viewPager = root.findViewById(R.id.view_pager);
        videoView = root.findViewById(R.id.myVideo);

        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

        String videoUri = "https://firebasestorage.googleapis.com/v0/b/infosucursaltv.appspot.com/o/media%2Fvideos%2Fvideo_financiera.mp4?alt=media&token=68ebb743-f190-4604-a533-29d3e5a09715";

        // Uri vidUri = Uri.parse(videoUri);

//        videoView.setVideoURI(vidUri);
//        videoView.requestFocus();
//        videoView.start();
        //Datos Firebase

        try{

            DatabaseReference videoRef = database.getReference("AppMedia/videos/video1/url");

            videoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(getContext(), dataSnapshot.toString(), Toast.LENGTH_LONG).show();
                    Uri vidUri = Uri.parse(String.valueOf(dataSnapshot.getValue()));

                    videoView.setVideoURI(vidUri);
                    videoView.requestFocus();
                    videoView.start();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.v("ErrorBD", "No hay DAtos");
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
