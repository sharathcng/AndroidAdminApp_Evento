package com.example.eventoadmin.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.eventoadmin.R;

public class SliderAdapter extends PagerAdapter  {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {

            R.drawable.viewevent,
            R.drawable.registerevent,
            R.drawable.manageevent,
            R.drawable.notifyevent

    };

    int headings[] = {

            R.string.slider1,
            R.string.slider2,
            R.string.slider3,
            R.string.slider4

    };

    int descriptions[] = {

            R.string.desc1,
            R.string.desc2,
            R.string.desc3,
            R.string.desc4

    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_page,container,false);

        ImageView imageView = view.findViewById(R.id.picture);
        TextView heading = view.findViewById(R.id.title);
        TextView desc = view.findViewById(R.id.description);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
