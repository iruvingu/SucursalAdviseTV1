package com.example.sucursaladvisetv;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private VideoView videoView;
    private MainCentralFragment mainCentralFragment;

    // Variables
    private String videoUriString = "";
    private int position;
    private int duration;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(Bundle bundleReceived) {
        VideoFragment videoFragment = new VideoFragment();
        if (bundleReceived != null) {
            videoFragment.setArguments(bundleReceived);
        }
        return videoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainCentralFragment = (MainCentralFragment) getParentFragment();
        position = 0;
        duration = 0;
        if (getArguments() != null) {
            videoUriString = getArguments().getString("uri_video");
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = view.findViewById(R.id.myVideo);

        Uri videoUri = Uri.parse(videoUriString);

        videoView.setVideoURI(videoUri);


        // This can Get the video's duration
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                duration = mediaPlayer.getDuration();

                Log.v("Video_Duration", String.valueOf(duration) );
                mainCentralFragment.changeDelay(duration);
                videoView.requestFocus();
                videoView.pause();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mainCentralFragment.onHandlerListener(position);
            }
        });

        return view;
    }

    public int getDuration(){
        return duration;
    }

    public void playVideoToFragment(){
        // Pausar el runnable
        mainCentralFragment.stopRunnable();
        Log.v("VideoView", "Video view: " + videoView);
        if (videoView != null){
            videoView.requestFocus();
            videoView.start();
            Log.v("AYUWOKI", "AYUWOKI IS PLAYINGS");
        }
        Log.v("AYUWOKI", "AYUWOKI IS PLAYING");
    }

}
