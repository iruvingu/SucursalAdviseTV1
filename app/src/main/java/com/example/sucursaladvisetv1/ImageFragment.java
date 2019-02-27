package com.example.sucursaladvisetv1;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    // Widgets
    private ImageView imageView;

    // Variables
    private String imageUrlString = "";

    public static ImageFragment newInstance(Bundle bundleReceived) {
        ImageFragment imageFragment = new ImageFragment();
        if (bundleReceived != null) {
            imageFragment.setArguments(bundleReceived);
        }
        return imageFragment;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrlString = getArguments().getString("uri_image");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        imageView = view.findViewById(R.id.image_view);

        Uri imageUri = Uri.parse(imageUrlString);

        Glide
                .with(view)
                .load(imageUri)
                .centerCrop()
                .into(imageView);

        return view;
    }

}
