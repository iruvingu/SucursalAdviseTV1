package com.example.sucursaladvisetv1;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FootScreenFragment extends Fragment {

    public FootScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Marquee
        TextView textView = (TextView) container.findViewById(R.id.label_marquee);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setText("General Information... general information... General Information....General Information... general information... General Information....General Information... general information... General Information....");
        textView.setSelected(true);
        textView.setSingleLine();
        return inflater.inflate(R.layout.fragment_foot_screen, container, false);
    }

}
