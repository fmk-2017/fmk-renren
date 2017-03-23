package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.everyoneassist.Adapter.SectionsPagerAdapter;
import com.example.everyoneassist.R;

public class GuideActivity extends BaseActivity {

    private ViewPager guide_viewpager;

    private ImageView[] imageViews;
    private int[] images = new int[]{R.mipmap.guide, R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);

        guide_viewpager = (ViewPager) this.findViewById(R.id.guide_viewpager);
        guide_viewpager.setOffscreenPageLimit(3);
        imageViews = new ImageView[images.length];
        for (int i = 0; i < images.length; i++) {
            imageViews[i] = new ImageView(this);
            imageViews[i].setImageResource(images[i]);
            if (i == (images.length - 1))
                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(v.getContext(), LoginActivity.class));
                        finish();
                    }
                });
        }
        guide_viewpager.setAdapter(new SectionsPagerAdapter(this, imageViews));

    }
}
