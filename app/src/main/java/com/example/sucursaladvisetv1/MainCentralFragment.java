package com.example.sucursaladvisetv1;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    private ViewPager viewPager;
    private MainCentralAdapter adapter;
    private Handler handler;
    private Context context;

    // Variables
    private int delay = 120000; //milliseconds
    private int page = 0;
    Boolean videosFlag = false;

    private ArrayList<MediaObject> listaObjetos = new ArrayList<MediaObject>();
    private ArrayList<Boolean> banderas = new ArrayList<Boolean>();

    //Firebase RTD
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Runable
    Runnable runnable = new Runnable() {
        public void run() {
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
        }
    };

    public MainCentralFragment() {
        // Required empty public constructor
    }

    public void onHandlerListener(){
        Toast.makeText(getContext(), "Se acabo el video", Toast.LENGTH_LONG).show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        handler = new Handler();
        viewPager = root.findViewById(R.id.view_pager);

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

    // Este metodo debe sacar y meter en la lista Objetos todos los datos
    public void addlist(){
        DatabaseReference databaseReference = database.getReference("app_media");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        for (DataSnapshot objectMedia : dataSnapshot.getChildren()){
                            listaObjetos.add(objectMedia.getValue(MediaObject.class));
                            banderas.add(false);
                        }
//                        for (int i = listaObjetos.size() - 1; i >= 0; i--) {
//                            if (listaObjetos.get(i).getTipo().equals("img")) {
//                                listaObjetos.remove(i);
//                            }
//                        }
                        for (int i = listaObjetos.size() - 1; i >= 0 ; i--) {
                            if (listaObjetos.get(i).getTipo().equals("video")) {
                                String localPath = downloadVideosLocal(listaObjetos.get(i).getUrl(),
                                        listaObjetos.get(i).getNombre());
                                // Log.v("localPath", localPath);

                                listaObjetos.get(i).setUrl(localPath);
                            }
                        }
                        Log.v("Lista", String.valueOf(listaObjetos.size()) );

                        //View Pager
                        adapter = new MainCentralAdapter(getChildFragmentManager());

                        viewPager.setAdapter(adapter);

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                Log.v("Postion", "Position" + position);
                                Log.v("ObjectListPosition", "Tipo es" + listaObjetos.get(position).getTipo());
                                if (listaObjetos.get(position).getTipo().equals("video")) {
                                    videosFlag = true;
                                    Log.v("videos_flag", String.valueOf(videosFlag));
                                } else {
                                    videosFlag = false;
                                }
                            }

                            @Override
                            public void onPageSelected(int position) {
                                page = position;
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

        // Log.v("PATH", Environment.DIRECTORY_DOWNLOADS + "/" + videoName);
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
                    bundle.putBoolean("boolean_video", videosFlag);
                    fragment = VideoFragment.newInstance(bundle);
                    fragments.put(position, fragment);
                }
            }
            return fragment;
        }
    }

}
