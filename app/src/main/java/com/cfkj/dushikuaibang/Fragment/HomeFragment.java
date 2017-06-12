package com.cfkj.dushikuaibang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.cfkj.dushikuaibang.Activity.MyInfoActivity;
import com.cfkj.dushikuaibang.Activity.ReleaseNeedTypeActivity;
import com.cfkj.dushikuaibang.Activity.ShoppingActivity;
import com.cfkj.dushikuaibang.Activity.SingleServerActivity;
import com.cfkj.dushikuaibang.Adapter.HeaderGridViewAdapter;
import com.cfkj.dushikuaibang.Adapter.HomeAdapter;
import com.cfkj.dushikuaibang.Adapter.SectionsPagerAdapter;
import com.cfkj.dushikuaibang.Entity.Home;
import com.cfkj.dushikuaibang.Entity.Skill;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;
import com.cfkj.dushikuaibang.Utils.LocationUtils;
import com.cfkj.dushikuaibang.Utils.ScreenUtils;
import com.cfkj.dushikuaibang.Utils.TimeUtils;
import com.cfkj.dushikuaibang.View.MyListView;
import com.cfkj.dushikuaibang.View.Pull.PullToRefreshBase;
import com.cfkj.dushikuaibang.View.Pull.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, HttpPostRequestUtils.HttpPostRequestCallback, AMapLocationListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static String METHOD_HOME = "get_home"; // 首页
    private static String METHOD_PRAISE = "user_praise"; // 赞
    Handler mhandler = new Handler();
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
    private List<Skill> skillList;
    private int[] image = new int[]{
            R.mipmap.homec1,
            R.mipmap.homec2,
            R.mipmap.homec3,
            R.mipmap.homec4,
    };
    private int curitem = 1;
    Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            if (curitem == 4)
                curitem = 0;

            header_viewpager.setCurrentItem(curitem);
            mhandler.postDelayed(mrunnable, 2500);
            ++curitem;
        }
    };

    private int page = 1;
    private boolean canload = true;

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
                page = 1;
                LocationUtils.getInstance(getActivity()).startLoaction(HomeFragment.this);
            }
        });
        myListView = homelistview.getRefreshableView();
        myListView.setbottom(DensityUtil.dip2px(15));
        myListView.addHeaderView(headerview);
        homelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                intent.putExtra("user_id", home.getGet_server_list().get(position - 2).getUser_id());
                intent.putExtra("skill_id", home.getGet_server_list().get(position - 2).getSkill_id());
                startActivity(intent);
            }
        });

        shared = getActivity().getSharedPreferences("location", Context.MODE_PRIVATE);
        lon = shared.getString("lon", "");
        lat = shared.getString("lat", "");
        if (!TextUtils.isEmpty(lon) && !TextUtils.isEmpty(lat))
            getHome(lat, lon);

        mhandler.removeCallbacks(mrunnable);
        mhandler.postDelayed(mrunnable, 2500);

        homelistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (skillList == null) return;
                if ((firstVisibleItem + visibleItemCount + 3) >= skillList.size() && canload) {
                    canload = false;
                    page++;
                    LocationUtils.getInstance(getActivity()).startLoaction(HomeFragment.this);
                }
            }
        });

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        curitem = position;
        mhandler.removeCallbacks(mrunnable);
        mhandler.postDelayed(mrunnable, 2500);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void initHeaderView() {
        header_viewpager = (ViewPager) headerview.findViewById(R.id.header_viewpager);
        header_viewpager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(getActivity()) / 4));

        header_viewpager.addOnPageChangeListener(this);

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
                if (position == 0 || position == 1) {
                    Intent intent = new Intent(getActivity(), ShoppingActivity.class);
                    intent.putExtra("type", home.getGet_category().get(position).getCat_id());
                    startActivityForResult(intent, 555);
                } else if (position == 7) {
                    Intent intent = new Intent(getActivity(), ReleaseNeedTypeActivity.class);
                    intent.putExtra("type", "all");
                    startActivity(intent);
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
            if (home.getGet_category() != null && home.getGet_category().size() > 0) {
                home.getGet_category().get(7).setCat_name("全部");
                header_gridview.setAdapter(new HeaderGridViewAdapter(getActivity(), home.getGet_category(), 8));
            }

            if (home.getGet_server_list() != null && home.getGet_server_list().size() > 0) {
                if (skillList == null) skillList = new ArrayList<>();
                if (page == 1) {
                    skillList.clear();
                }
                skillList.addAll(home.getGet_server_list());
                if (homeAdapter != null) homeAdapter.notifyDataSetChanged();
                else {
                    homeAdapter = new HomeAdapter(getActivity(), skillList, this);
                    homelistview.setAdapter(homeAdapter);
                }
                canload = true;
            }
            Log.e("sss", page + " hah " + home.getGet_server_list().size());
            if (home.getHome_pic() != null && home.getHome_pic().size() > 0) {
//                ImageView[] imageViews = new ImageView[home.getHome_pic().size()];
//                for (int i = 0; i < home.getHome_pic().size(); i++) {
//                    imageViews[i] = new ImageView(getActivity());
//                    x.image().bind(imageViews[i], home.getHome_pic().get(i).getAd_photo());
//                }
                ImageView[] imageViews = new ImageView[4];
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[i] = new ImageView(getActivity());
                    imageViews[i].setImageResource(image[i]);
                }
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            } else {
                ImageView[] imageViews = new ImageView[1];
                imageViews[0] = new ImageView(getActivity());
                imageViews[0].setImageResource(R.mipmap.home_03);
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            }
        } else if (METHOD_PRAISE.equals(method)) {
            getHome(lat, lon);
            Log.e(METHOD_PRAISE, "赞成功or取消赞");
        }
    }

    @Override
    public void Fail(String method, String error) {
        homelistview.onRefreshComplete();
        if (METHOD_HOME.equals(method)) {
            if (page > 1) page--;
        }
//        if (x.isDebug()) Toast.makeText(getApplicationContext(), this.getClass().getName() + ": " + method + " error: " + error, Toast.LENGTH_SHORT).show();
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
        map.put("page", page + "");
        map.put("category_id", "0");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeitem_zan:
                String skillid = (String) v.getTag();
                zan(skillid);
                break;
        }
    }

    public void zan(String skillid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_PRAISE);
        map.put("skill_id", skillid);
        map.put("userid", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

}
