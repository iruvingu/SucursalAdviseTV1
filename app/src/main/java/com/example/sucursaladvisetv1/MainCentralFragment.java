package com.example.sucursaladvisetv1;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private CustomSwipeAadapter adapter;
    private VideoView videoView;
    private Handler handler;
    private Context context;
    private int delay = 5000; //milliseconds
    private int page = 0;

    int duration = 0;

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

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);

        handler = new Handler();
        viewPager = root.findViewById(R.id.view_pager);
        videoView = root.findViewById(R.id.myVideo);

        //View Pager
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

    // Este metodo debe sacar y meter en la lista todos los datos
    public void addlist(){
        DatabaseReference databaseReference = database.getReference("appmedia");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try{
                        for (DataSnapshot getImagesName : dataSnapshot.getChildren()){
                            for (DataSnapshot getUrl : getImagesName.getChildren()){
                                switch (getUrl.getKey()){
                                    case "url":
                                        listaImagenesUrl.add(getUrl.getValue(String.class).trim());
                                        break;
                                }
                                Log.d("KEY", "LAS URL: " + listaImagenesUrl);
                                notifyDataSetChanged();
                            }
                        }


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
            return getFragment(infoListShowInThis.get(position), position);
        }

        // Cuenta el tamaño de las imagenes y videos
        @Override
        public int getCount() {
            return infoListShowInThis.size();
        }

        //gral metods
        private Fragment getFragment(String nameImage, int position){
            Fragment fragment = fragments.get(position);
            if(fragment==null){

                Bundle bundle = new Bundle();
                bundle.putString("urlImage", nameImage);
                fragment = ShowImageFragment.newInstance(bundle);
                fragments.put(position, fragment);

            }
            return fragment;
        }
    }

}
