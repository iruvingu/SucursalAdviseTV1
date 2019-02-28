package com.example.sucursaladvisetv1;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private int delay = 5000; //milliseconds
    private int page = 0;

    private ArrayList<MediaObject> listaObjetos = new ArrayList<MediaObject>();

    //Firebase RTD
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
                        }

                        //View Pager
                        adapter = new MainCentralAdapter(getChildFragmentManager());

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

                    }catch(Exception e){

                    }
                }else{

                }
                return ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                else if (mediaObject.getTipo().equals("video")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("uri_video", mediaObject.getUrl());
                    fragment = VideoFragment.newInstance(bundle);
                    fragments.put(position, fragment);
                }

            }
            return fragment;
        }
    }

}
