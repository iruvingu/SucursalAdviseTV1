//package com.example.sucursaladvisetv1;
//
//import android.content.Context;
//import android.net.Uri;
//import android.nfc.Tag;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.PagerAdapter;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomSwipeAadapter extends PagerAdapter {
//    //private String [] image_resource;= {R.drawable.finaciera_plan,R.drawable.financiera_bancos,R.drawable.financiera_independencia};
//    private Context context;
//
//    private LayoutInflater layoutInflater;
//    private DatabaseReference myBaseReference;
//
//
//    public CustomSwipeAadapter(Context context){
//        this.context=context;
//        addlist();
//    }
//
//    @Override
//    public int getCount() {
//        return listaImagenesUrl.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object o) {
//
//        return (view == (LinearLayout)o);
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//
//        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View item_view = layoutInflater.inflate(R.layout.fragment_image,container,false);
//        ImageView imageView = item_view.findViewById(R.id.image_view);
//
//        Glide.with(item_view).load(Uri.parse(listaImagenesUrl.get(position))).centerCrop().into(imageView);
//        //imageView.setImageResource(image_resource[position]);
//        container.addView(item_view);
//
//        return item_view;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((LinearLayout)object);
//    }
//
//
//
//}
