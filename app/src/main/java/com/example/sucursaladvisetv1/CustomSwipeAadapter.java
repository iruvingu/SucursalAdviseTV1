package com.example.sucursaladvisetv1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomSwipeAadapter extends PagerAdapter {
    private int[] image_resource = {R.drawable.finaciera_plan,R.drawable.financiera_bancos,R.drawable.financiera_independencia};
    private Context context;
    private List<String> listaImagenesUrl = new ArrayList<String>();
    private LayoutInflater layoutInflater;

    public CustomSwipeAadapter(Context context, List<String> lista){
        this.context = context;
        this.listaImagenesUrl = lista;
    }

    @Override
    public int getCount() {
        return listaImagenesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {

        return (view == (LinearLayout)o);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);

        imageView.setImageResource(Integer.parseInt(listaImagenesUrl.get(position)));
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
