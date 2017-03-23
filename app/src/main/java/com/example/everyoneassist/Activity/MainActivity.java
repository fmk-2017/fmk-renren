package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.everyoneassist.Adapter.MainPagerAdapter;
import com.example.everyoneassist.Fragment.HomeFragment;
import com.example.everyoneassist.Fragment.MeFragment;
import com.example.everyoneassist.Fragment.ReceivingFragment;
import com.example.everyoneassist.Fragment.SkillFragment;
import com.example.everyoneassist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageView hire;
    private List<Fragment> fragmentlist;
    private TextView[] textviews = new TextView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentlist = new ArrayList<>();
        fragmentlist.add(HomeFragment.newInstance());
        fragmentlist.add(ReceivingFragment.newInstance());
        fragmentlist.add(SkillFragment.newInstance());
        fragmentlist.add(MeFragment.newInstance());

        mSectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragmentlist);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        textviews[0] = (TextView) this.findViewById(R.id.table1);
        textviews[1] = (TextView) this.findViewById(R.id.table2);
        textviews[2] = (TextView) this.findViewById(R.id.table4);
        textviews[3] = (TextView) this.findViewById(R.id.table5);

        for (TextView textview : textviews) {
            textview.setOnClickListener(this);
        }

        hire = (ImageView) this.findViewById(R.id.hire);
        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ReleaseNeedTypeActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.table1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.table2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.table4:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.table5:
                mViewPager.setCurrentItem(3);
                break;
        }
    }
}
