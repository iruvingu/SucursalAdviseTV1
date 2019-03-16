package com.example.sucursaladvisetv;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
import java.io.IOException;
import java.util.ArrayList;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;
import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    private ViewPager viewPager;
    private MainCentralAdapter adapter;
    private Handler handler;
    private Context context;
    private VideoFragment videoFragment;

    // Variables
    private int delay = 20000; //milliseconds
    private int page = 0;

    private ArrayList<MediaObject> listaObjetos = new ArrayList<MediaObject>();

    //Firebase RTD
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //Runable
    Runnable runnable = new Runnable() {
        public void run() {
            Log.v("LADB", "Delay: " + delay);
            if (adapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            try {
                viewPager.setCurrentItem(page, true);
            } finally {
                handler.postDelayed(runnable, delay);
            }
            Log.v("LADB", "Delay: " + delay);
        }
    };

    public MainCentralFragment() {
        // Required empty public constructor
    }


    public void changeDelay(int duration) {
        this.delay = duration + 10000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        handler = new Handler();
        videoFragment = new VideoFragment();
        viewPager = root.findViewById(R.id.view_pager);

        // This enable swiping manually
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        addlist();

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

    // Este metodo debe sacar y meter en la lista Objetos de tipo MediaObject todos los datos
    public void addlist(){
        DatabaseReference databaseReference = database.getReference("app_media");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        for (DataSnapshot objectMedia : dataSnapshot.getChildren()){
                            listaObjetos.add(objectMedia.getValue(MediaObject.class));

                        }
                        for (int i = listaObjetos.size() - 1; i >= 0 ; i--) {
                            if (listaObjetos.get(i).getTipo().equals("video")) {
                                String localPath = downloadVideosLocal(listaObjetos.get(i).getUrl(),
                                        listaObjetos.get(i).getNombre());
                                listaObjetos.get(i).setUrl(localPath);
                            }
                        }
                        //View Pager
                        adapter = new MainCentralAdapter(getChildFragmentManager());

                        viewPager.setAdapter(adapter);

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                if (listaObjetos.get(position).getTipo().equals("video")) {
                                    videoFragment = (VideoFragment) adapter.getFragment(position);
                                    if (positionOffset != 0.0){
                                        videoFragment.stopVideoToFragment();
                                    }else{
                                        videoFragment.playVideoToFragment();
                                    }
                                }  else {
                                    int next = (position + 1 == adapter.getCount()) ? 0 : position + 1;

                                    if (listaObjetos.get(next).getTipo().equals("video")) {
                                        if(positionOffset != 0.0){
                                            videoFragment.stopVideoToFragment();
                                        }
                                    }
                                    delay = 20000;
                                }
                            }

                            @Override
                            public void onPageSelected(int position) {
                                page = position;
                                if (listaObjetos.get(position).getTipo().equals("video")) {
                                    Log.v("LADB", "Delay: " + delay);
                                    videoFragment = (VideoFragment) adapter.getFragment(position);
                                    videoFragment.playVideoToFragment();
                                } else {
                                    delay = 20000;
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });


                    }catch(Exception e){
                        Log.d("Error", "Error: No existen datos");
                    }
                }else{
                    Log.d("Error", "Error: No existen datos");
                }
                return ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error", "Error: No existen datos");
            }
        });

    }

    private String downloadVideosLocal(String url, String videoName) {
        // Getting the data from Storage from url
        StorageReference videoRef = storage.getReferenceFromUrl(url);

        // File apkStorage = new File(Environment.DIRECTORY_DOWNLOADS + "/" + videoName);

        try {
            File localFile = File.createTempFile("videos",".mp4");

            videoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.v("VideoDownloaded", "The video was downloaded");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Loading", "El video se esta cargando");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors
                    Log.v("Fail", "File Failure");
                }
            });
            Log.v("AbstractPath", localFile.getAbsolutePath());
            return localFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class MainCentralAdapter extends FragmentStatePagerAdapter {

        //
        private SparseArray<Fragment> fragments = new SparseArray<>();


        //constructor
        public MainCentralAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getFragment(int positionFragment){
            return fragments.get(positionFragment);
        }

        //overrideMethods
        @Override
        public Fragment getItem(int position) {
            return getFragment(listaObjetos.get(position), position);
        }

        // Cuenta el tamaño de las imagenes y videos
        @Override
        public int getCount() {

            return listaObjetos.size();
        }

        //gral metods
        private Fragment getFragment(MediaObject mediaObject, int position){
            Fragment fragment = fragments.get(position);
            if(fragment == null){
                if (mediaObject.getTipo().equals("img")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uri_image", mediaObject.getUrl());
                    fragment = ImageFragment.newInstance(bundle);
                    fragments.put(position, fragment);
                }
                else
            if (mediaObject.getTipo().equals("video")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uri_video", mediaObject.getUrl());
                    bundle.putInt("position", position);
                    fragment = VideoFragment.newInstance(bundle);
                    fragments.put(position, fragment);
                }
            }
            return fragment;
        }
    }


}
