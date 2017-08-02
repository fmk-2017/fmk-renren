package com.example.dushikuaibang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.CircularArray;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.dushikuaibang.Activity.AtWillBuyActivity;
import com.example.dushikuaibang.Adapter.ReceivingAdapter;
import com.example.dushikuaibang.Entity.Demand;
import com.example.dushikuaibang.Entity.GoodsNameAndId;
import com.example.dushikuaibang.Interface.OnInvitedListener;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.DebugLog;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.WindowsUtils;
import com.example.dushikuaibang.View.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReceivingFragment extends Fragment implements LocationSource, AMapLocationListener, HttpPostRequestUtils.HttpPostRequestCallback, View.OnClickListener, OnInvitedListener, AbsListView.OnScrollListener {
    private final String METHOD_DEMAND = "demand_list";
    private final String APPOINTMENT_SERVIER = "appointment_server";
    private MapView d2map;
    private MyListView receiving_listview;
    private ReceivingAdapter receivingAdapter;
    private AMap aMap;
    private TextView type, mode;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private List<GoodsNameAndId> list = new ArrayList<>();
    private List<Demand> demandList;
    private String server_type = "";
    private String category_id = "";
    private List<GoodsNameAndId> nameAndIds;
    private double latitude, longitude;
    private String sexs;

    public ReceivingFragment() {
    }

    public static ReceivingFragment newInstance() {
        ReceivingFragment fragment = new ReceivingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receiving, container, false);
        d2map = (MapView) view.findViewById(R.id.d2map);

        type = (TextView) view.findViewById(R.id.type);
        mode = (TextView) view.findViewById(R.id.mode);
        type.setOnClickListener(this);
        mode.setOnClickListener(this);

        receiving_listview = (MyListView) view.findViewById(R.id.receiving_listview);
        receiving_listview.setbottom(DensityUtil.dip2px(15));
        receiving_listview.setOnScrollListener(this);

        d2map.onCreate(savedInstanceState);// 此方法必须重写
        aMap = d2map.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location));
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);

        list.add(new GoodsNameAndId("不限", 0));
        list.add(new GoodsNameAndId("随意购", 1));
        list.add(new GoodsNameAndId("同城快递", 2));
        list.add(new GoodsNameAndId("休闲娱乐", 3));
        list.add(new GoodsNameAndId("运动健康", 4));
        list.add(new GoodsNameAndId("丽人时尚", 5));
        list.add(new GoodsNameAndId("智慧社区", 6));
        list.add(new GoodsNameAndId("家政维修", 7));
        list.add(new GoodsNameAndId("教育培训", 8));
        list.add(new GoodsNameAndId("咨询服务", 9));
        list.add(new GoodsNameAndId("技术服务", 10));
        list.add(new GoodsNameAndId("智慧高校", 11));
        list.add(new GoodsNameAndId("车务服务", 12));

        return view;
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
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(2000);
            mlocationClient.setLocationOption(mLocationOption);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mlocationClient.stopLocation();
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                getdemand(latitude, longitude);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                DebugLog.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        d2map.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        deactivate();
    }

    @Override
    public void onResume() {
        super.onResume();
        d2map.onResume();
        if (mlocationClient != null)
            mlocationClient.startLocation();//启动定位
    }

    @Override
    public void onPause() {
        super.onPause();
        d2map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        d2map.onSaveInstanceState(outState);
    }

    public void getdemand(double user_lat, double user_lon) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_DEMAND);
        map.put("category_id", category_id);
        map.put("user_lat", user_lat + "");
        map.put("user_lon", user_lon + "");
        map.put("server_type", server_type);
        map.put("server_sex", sexs);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_DEMAND.equals(method)) {
            receiving_listview.setVisibility(View.VISIBLE);
            if (demandList == null || demandList.size() == 0) {
                demandList = JSON.parseArray(json.getString("data"), Demand.class);
                receivingAdapter = new ReceivingAdapter(getActivity(), demandList, this);
                receiving_listview.setAdapter(receivingAdapter);
            } else {
                demandList.clear();
                demandList.addAll(JSON.parseArray(json.getString("data"), Demand.class));
                receivingAdapter.notifyDataSetChanged();
            }
            setHomeDate();
            addmarket();
        } else if (APPOINTMENT_SERVIER.equals(method)) {

        }
    }

    public void setHomeDate() {
        CircularArray<HomeFragment.HomeMessage> homemessage = new CircularArray<>();
        for (int i = 0; i < (demandList.size() > 10 ? 10 : demandList.size()); i++) {
            HomeFragment.HomeMessage hmessage = new HomeFragment.HomeMessage();
            hmessage.setNickname(demandList.get(i).getNickname());
            hmessage.setUser_name(demandList.get(i).getUser_name());
            hmessage.setCat_name(demandList.get(i).getCat_name());
            hmessage.setId(demandList.get(i).getId());
            hmessage.setCategory_id(demandList.get(i).getCategory_id());
            homemessage.addLast(hmessage);
        }
        HomeFragment.getInstance().setHomePagerData(homemessage);
    }

    public void addmarket() {
        for (Demand demand : demandList) {
            if (TextUtils.isEmpty(demand.getServer_lon())) return;
            float v = Float.valueOf(demand.getServer_lat());
            float v1 = Float.valueOf(demand.getServer_lon());
            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(v, v1));
            mo.draggable(false);
            mo.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.zhen)));
            aMap.addMarker(mo);
        }
    }

    @Override
    public void Fail(String method, String error) {
        if (METHOD_DEMAND.equals(method)) {
            receiving_listview.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "暂无需求", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type:
                WindowsUtils.showStringListPopupWindow(v, list, new WindowsUtils.OnStringItemClickListener() {
                    @Override
                    public void onStringItemClick(int position, int sex) {
                        category_id = position + "";
                        sexs = sex + "";
                        getdemand(latitude, longitude);
                    }
                });
                break;
            case R.id.mode:
                List<GoodsNameAndId> nameAndIds = new ArrayList<GoodsNameAndId>();
                nameAndIds.add(new GoodsNameAndId("Ta来找我", 1));
                nameAndIds.add(new GoodsNameAndId("我去找TA", 2));
                nameAndIds.add(new GoodsNameAndId("线上服务", 3));
                WindowsUtils.showStringListPopupWindow(v, nameAndIds, new WindowsUtils.OnStringItemClickListener() {
                    @Override
                    public void onStringItemClick(int position, int sex) {
                        server_type = (position + 1) + "";
                        sexs = sex + "";
                        getdemand(latitude, longitude);
                    }
                });
                break;
            case R.id.receiving:
                int position = (int) v.getTag();
                Intent intent = new Intent(getContext(), AtWillBuyActivity.class);
                intent.putExtra("xid", demandList.get(position).getId());
                intent.putExtra("title", demandList.get(position).getCat_name());
                intent.putExtra("category_id", demandList.get(position).getCategory_id());
                intent.putExtra("name", demandList.get(position).getNickname());
                intent.putExtra("avatar", demandList.get(position).getUser_photo());
                startActivity(intent);
                break;
        }
    }

    public void add(List<Demand> demandList) {
        for (Demand demand : demandList) {
            float lat = Float.valueOf(demand.getServer_lat());
            float lon = Float.valueOf(demand.getServer_lon());
            MarkerOptions mo = new MarkerOptions();
            mo.anchor(lat, lon);
            aMap.addMarker(mo);
        }
    }


    //立即应邀
    @Override
    public void onInvited(int position) {
        if (demandList != null) {
            Intent intent = new Intent(getContext(), AtWillBuyActivity.class);
            intent.putExtra("xid", demandList.get(position).getId());
            startActivity(intent);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (demandList == null || demandList.size() <= 0) return;
        Demand demand = demandList.get(firstVisibleItem);
        if (TextUtils.isEmpty(demand.getServer_lat())) return;
        float lat = Float.valueOf(demand.getServer_lat());
        float lon = Float.valueOf(demand.getServer_lon());
        LatLng latLng = new LatLng(lat, lon);
        CameraUpdate u = CameraUpdateFactory.newLatLngZoom(latLng, 17.0f);
        aMap.animateCamera(u);
    }
}
