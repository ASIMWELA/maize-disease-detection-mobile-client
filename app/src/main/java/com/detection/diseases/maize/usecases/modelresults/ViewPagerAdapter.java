package com.detection.diseases.maize.usecases.modelresults;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.detection.diseases.maize.R;

import java.util.Objects;

class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int[] images;
    LayoutInflater mLayoutInflater;
    public ViewPagerAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((CardView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml 
        View itemView = mLayoutInflater.inflate(R.layout.modelr_results_view_pager_container, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.single_image_view);
        imageView.setImageResource(images[position]);
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}