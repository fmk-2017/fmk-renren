package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.everyoneassist.Activity.MainActivity;
import com.example.everyoneassist.Fragment.PlaceholderFragment;
import com.example.everyoneassist.R;

import java.util.List;

/**
 * Created by fengm on 2017/1/12.
 */

public class SectionsPagerAdapter extends PagerAdapter {

    private Context context;
    private ImageView[] imageViews;

    public SectionsPagerAdapter(Context context, ImageView[] imageViews) {
        this.context = context;
        this.imageViews = imageViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViews[position];
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeViewAt(position);
    }

    @Override
    public int getCount() {
        return imageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
