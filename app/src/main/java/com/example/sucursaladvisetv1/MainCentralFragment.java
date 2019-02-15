package com.example.sucursaladvisetv1;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainCentralFragment extends Fragment {

    ViewPager viewPager;
    CustomSwipeAadapter adapter;
    private Context context;

    public MainCentralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewPager = (ViewPager) viewPager.findViewById(R.id.view_pager);
        adapter = new CustomSwipeAadapter(getActivity());
        viewPager.setAdapter(adapter);
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(), 2000, 4000);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main_central, container, false);



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
