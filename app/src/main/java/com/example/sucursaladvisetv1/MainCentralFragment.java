package com.example.sucursaladvisetv1;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    private ViewPager viewPager;
    private CustomSwipeAadapter adapter;
    private VideoView videoView;
    ProgressBar progressBar;
    private Context context;
    private Handler handler;
    private int delay = 5000; //milliseconds
    private int page = 0;

    List<String> imageUrlList = new ArrayList<String>();

    Boolean isPlaying;
    int current = 0, duration = 0;

    //Firebase RTD
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Runable
    Runnable runnable = new Runnable() {
        public void run() {
            if (adapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(runnable, delay);
        }
    };

    public MainCentralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isPlaying = false;

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        handler = new Handler();
        viewPager = root.findViewById(R.id.view_pager);
        videoView = root.findViewById(R.id.myVideo);

        // Datos Firebase

        try{

            DatabaseReference videoRef = database.getReference("AppMedia/videos/video1/url");

            videoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Uri vidUri = Uri.parse(String.valueOf(dataSnapshot.getValue()));

                    videoView.setVideoURI(vidUri);
                    videoView.requestFocus();

                    // Get the video's duration
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            duration = mp.getDuration() / 1000;
                            String durationString = String.format("%02d:%02d", duration / 60, duration % 60);
                        }
                    });

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

        //View Pager
        videoView.setVisibility(View.GONE);
        adapter = new CustomSwipeAadapter(getActivity());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Direccion remota de dominio publico aqui tendra q ir nuestra direccion de firebase


        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public  class VideoProgress extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

}
