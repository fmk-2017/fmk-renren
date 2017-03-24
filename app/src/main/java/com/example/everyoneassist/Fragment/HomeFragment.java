package com.example.everyoneassist.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.everyoneassist.Activity.AtWillBuyActivity;
import com.example.everyoneassist.Activity.MainActivity;
import com.example.everyoneassist.Activity.ShoppingActivity;
import com.example.everyoneassist.Activity.SingleServerActivity;
import com.example.everyoneassist.Adapter.HeaderGridViewAdapter;
import com.example.everyoneassist.Adapter.HomeAdapter;
import com.example.everyoneassist.Adapter.SectionsPagerAdapter;
import com.example.everyoneassist.Entity.Home;
import com.example.everyoneassist.Entity.HomeCategory;
import com.example.everyoneassist.Entity.Pic;
import com.example.everyoneassist.Entity.Skill;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.LocationUtils;
import com.example.everyoneassist.Utils.ScreenUtils;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.MyListView;
import com.example.everyoneassist.View.Pull.PullToRefreshBase;
import com.example.everyoneassist.View.Pull.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, HttpPostRequestUtils.HttpPostRequestCallback, AMapLocationListener {

    private static String METHOD_HOME = "get_home"; // 首页
    private GridView header_gridview;//分类列表
    private String lon;
    private String lat;
    private SharedPreferences shared;
    private PullToRefreshListView homelistview;
    private MyListView myListView;
    private View headerview;
    private HomeAdapter homeAdapter;
    private ViewPager header_viewpager;
    private Home home;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocationUtils.getInstance(getActivity()).startLoaction(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        headerview = inflater.inflate(R.layout.home_header_layout, null, false);
        initHeaderView();
        homelistview = (PullToRefreshListView) view.findViewById(R.id.homelistview);
        homelistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<MyListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<MyListView> refreshView) {
                String label = TimeUtils.getFormatTime((System.currentTimeMillis() / 1000) + "");
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                LocationUtils.getInstance(getActivity()).startLoaction(HomeFragment.this);
            }
        });
        myListView = homelistview.getRefreshableView();
        myListView.setbottom(DensityUtil.dip2px(15));
        myListView.addHeaderView(headerview);

        shared = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        lon = shared.getString("lon", "");
        lat = shared.getString("lat", "");
        if (!TextUtils.isEmpty(lon) && !TextUtils.isEmpty(lat))
            getHome(lat, lon);
        return view;
    }

    private void initHeaderView() {
        header_viewpager = (ViewPager) headerview.findViewById(R.id.header_viewpager);
        header_viewpager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(getActivity()) / 4));

        header_gridview = (GridView) headerview.findViewById(R.id.header_gridview);
        header_gridview.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.header_gridview:
                if (position == 0 || position == 1) {//随意购
                    Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                    startActivityForResult(intent,555);
                } else {
                    Intent intent = new Intent(getActivity(), SingleServerActivity.class);
                    intent.putExtra("cid", home.getGet_category().get(position).getCat_id());
                    intent.putExtra("cname", home.getGet_category().get(position).getCat_name());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        homelistview.onRefreshComplete();
        if (METHOD_HOME.equals(method)) {
            home = JSON.parseObject(json.getString("data"), Home.class);
            if (home.getGet_category() != null && home.getGet_category().size() > 0)
                header_gridview.setAdapter(new HeaderGridViewAdapter(getActivity(), home.getGet_category(), 8));

            if (home.getGet_server_list() != null && home.getGet_server_list().size() > 0) {
                homeAdapter = new HomeAdapter(getActivity(), home.getGet_server_list());
                homelistview.setAdapter(homeAdapter);
            }
            if (home.getHome_pic() != null && home.getHome_pic().size() > 0) {
                ImageView[] imageViews = new ImageView[home.getHome_pic().size()];
                for (int i = 0; i < home.getHome_pic().size(); i++) {
                    imageViews[i] = new ImageView(getActivity());
                    x.image().bind(imageViews[i], home.getHome_pic().get(i).getAd_photo());
                }
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            } else {
                ImageView[] imageViews = new ImageView[1];
                imageViews[0] = new ImageView(getActivity());
                imageViews[0].setImageResource(R.mipmap.home_03);
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            }
        } else {

        }
    }

    @Override
    public void Fail(String method, String error) {
        homelistview.onRefreshComplete();
//        if (x.isDebug()) Toast.makeText(getActivity(), this.getClass().getName() + ": " + method + " error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        shared.edit().putString("lat", aMapLocation.getLatitude() + "").putString("lon", aMapLocation.getLongitude() + "").commit();
        getHome(aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
    }

    private void getHome(String user_lat, String user_lon) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_HOME);
        map.put("user_lat", user_lat);
        map.put("user_lon", user_lon);
        map.put("page", "1");
        map.put("category_id", "0");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

}
