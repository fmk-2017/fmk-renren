package com.example.dushikuaibang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.dushikuaibang.Activity.AtWillBuyActivity;
import com.example.dushikuaibang.Activity.MyInfoActivity;
import com.example.dushikuaibang.Activity.ReleaseNeedTypeActivity;
import com.example.dushikuaibang.Activity.SingleServerActivity;
import com.example.dushikuaibang.Adapter.HeaderGridViewAdapter;
import com.example.dushikuaibang.Adapter.HomeAdapter;
import com.example.dushikuaibang.Adapter.SectionsPagerAdapter;
import com.example.dushikuaibang.Entity.Home;
import com.example.dushikuaibang.Entity.HomeCategory;
import com.example.dushikuaibang.Entity.Skill;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.LocationUtils;
import com.example.dushikuaibang.Utils.ScreenUtils;
import com.example.dushikuaibang.Utils.TimeUtils;
import com.example.dushikuaibang.View.MyListView;
import com.example.dushikuaibang.View.Pull.PullToRefreshBase;
import com.example.dushikuaibang.View.Pull.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, HttpPostRequestUtils.HttpPostRequestCallback, AMapLocationListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static String METHOD_HOME = "get_home"; // 首页
    private static String METHOD_PRAISE = "user_praise"; // 赞
    private static HomeFragment fragment;
    Handler mhandler = new Handler();
    Handler mlisthandler = new Handler();
    private GridView header_gridview;//分类列表
    private String lon;
    private String lat;
    private SharedPreferences shared;
    private PullToRefreshListView homelistview;
    private MyListView myListView;
    private View headerview;
    private HomeAdapter homeAdapter;
    private ViewPager header_viewpager;
    private ListView message_list;
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
    private int zanposition;
    private String user_id;
    private int cir_item = 0;
    Runnable mRunna = new Runnable() {
        @Override
        public void run() {
            cir_item++;
            if (cir_item == message_list.getAdapter().getCount())
                cir_item = 0;
            message_list.smoothScrollToPositionFromTop(cir_item, 0);
            Log.e("sssss", cir_item + " == " + (System.currentTimeMillis()));
            mlisthandler.removeCallbacks(mRunna);
            mlisthandler.postDelayed(mRunna, 1500);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public static HomeFragment getInstance() {
        return fragment;
    }

    public void setHomePagerData(final CircularArray<HomeMessage> homemessage) { //message_viewpager
        if (homemessage != null && homemessage.size() > 0) {
            message_list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return homemessage.size();
                }

                @Override
                public Object getItem(int position) {
                    return homemessage.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ViewHolder vh;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_blank, null, false);
                        vh = new ViewHolder();
                        vh.onlytext = (TextView) convertView.findViewById(R.id.onlytext);
                        convertView.setTag(vh);
                    } else {
                        vh = (ViewHolder) convertView.getTag();
                    }
                    String name = AppUtils.AuthorPhone(homemessage.get(position).getNickname()) ? homemessage.get(position).getNickname().substring(0, 5) + "******" : homemessage.get(position).getNickname();

                    String showtext = name + " 发布了一条 " + homemessage.get(position).getCat_name() + "的需求";
                    vh.onlytext.setText(showtext);
                    return convertView;
                }

                class ViewHolder {
                    TextView onlytext;
                }
            });
            mlisthandler.removeCallbacks(mRunna);
            mlisthandler.postDelayed(mRunna, 1500);
        } else message_list.setVisibility(View.GONE);

        message_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AtWillBuyActivity.class);
                intent.putExtra("xid", homemessage.get(position).getId());
                intent.putExtra("title", homemessage.get(position).getCat_name());
                intent.putExtra("category_id", homemessage.get(position).getCategory_id());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.fragment = this;
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
                intent.putExtra("user_id", skillList.get(position - 2).getUser_id());
                intent.putExtra("skill_id", skillList.get(position - 2).getSkill_id());
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

        message_list = (ListView) headerview.findViewById(R.id.message_list);

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
                if (position == 7) {
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
                if (home.getGet_category().size() < 8) {
                    HomeCategory hc = new HomeCategory();
                    hc.setCat_name("全部");
                    home.getGet_category().add(hc);
                } else home.getGet_category().get(7).setCat_name("全部");
                header_gridview.setAdapter(new HeaderGridViewAdapter(getActivity(), home.getGet_category(), 8, true));
            }
            if (skillList == null) skillList = new ArrayList<>();
            if (home.getGet_server_list() != null && home.getGet_server_list().size() > 0) {
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
            } else {
                if (homeAdapter != null) homeAdapter.notifyDataSetChanged();
                else {
                    homeAdapter = new HomeAdapter(getActivity(), skillList, this);
                    homelistview.setAdapter(homeAdapter);
                }
            }
            Log.e("sss", page + " hah " + home.getGet_server_list().size());
            if (home.getHome_pic() != null && home.getHome_pic().size() > 0) {
                ImageView[] imageViews = new ImageView[home.getHome_pic().size()];
                for (int i = 0; i < home.getHome_pic().size(); i++) {
                    imageViews[i] = new ImageView(getActivity());
                    imageViews[i].setTag(home.getHome_pic().get(i).getUrl());
                    ImageLoader.getInstance().displayImage(home.getHome_pic().get(i).getAd_photo(), imageViews[i], AppUtils.getOptions());
                    imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = (String) v.getTag();
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            startActivity(intent);
                        }
                    });
                }
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            } else {
                ImageView[] imageViews = new ImageView[1];
                imageViews[0] = new ImageView(getActivity());
                imageViews[0].setImageResource(R.mipmap.home_03);
                header_viewpager.setAdapter(new SectionsPagerAdapter(getActivity(), imageViews));
            }
        } else if (METHOD_PRAISE.equals(method)) {
            if (skillList.get(zanposition).getIs_praise() == 1) {
                skillList.get(zanposition).setIs_praise(0);
                skillList.get(zanposition).setPraisesum(skillList.get(zanposition).getPraisesum() - 1);
            } else {
                skillList.get(zanposition).setIs_praise(1);
                skillList.get(zanposition).setPraisesum(skillList.get(zanposition).getPraisesum() + 1);
            }
            homeAdapter.notifyDataSetChanged();
            Log.e(METHOD_PRAISE, "赞成功or取消赞");
        }
    }

    @Override
    public void Fail(String method, String error) {
        homelistview.onRefreshComplete();
        if (METHOD_HOME.equals(method)) {
            if (page > 1) page--;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (String.valueOf(aMapLocation.getLatitude()).length() > 12) return;
        shared.edit().putString("lat", aMapLocation.getLatitude() + "").putString("lon", aMapLocation.getLongitude() + "").commit();
        Log.e("ssss", aMapLocation.getLatitude() + " " + aMapLocation.getLongitude());
        getHome(aMapLocation.getLatitude() + "", aMapLocation.getLongitude() + "");
    }

    private void getHome(String user_lat, String user_lon) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_HOME);
        map.put("user_lat", user_lat);
        map.put("user_lon", user_lon);
        map.put("page", page + "");
        map.put("category_id", "0");
        map.put("user_id", getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeitem_zan:
                zanposition = (int) v.getTag();
                String skillid = skillList.get(zanposition).getSkill_id();
                user_id = getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", "");
                if (TextUtils.isEmpty(user_id)) {
                    Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                zan(skillid);
                break;
        }
    }

    public void zan(String skillid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_PRAISE);
        map.put("skill_id", skillid);
        map.put("userid", user_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }


    public static class HomeMessage implements Serializable {

        private String nickname;
        private String user_name;
        private String cat_name;
        private String id;
        private String category_id;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }
    }

}
