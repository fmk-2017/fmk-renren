package com.example.dushikuaibang.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by fengm on 2017/1/12.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentlist;

    public MainPagerAdapter(FragmentManager fm, List<Fragment> fragmentlist) {
        super(fm);
        this.fragmentlist = fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return fragmentlist.size();
    }

}
