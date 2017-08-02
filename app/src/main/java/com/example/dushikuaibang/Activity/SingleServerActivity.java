package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.dushikuaibang.Adapter.ReceivingAdapter;
import com.example.dushikuaibang.Entity.Demand;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.LocationUtils;
import com.example.dushikuaibang.View.Pull.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class SingleServerActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, AMapLocationListener, View.OnClickListener {

    private final String METHOD_DEMAND = "demand_list";
    private PullToRefreshListView refreshlistview;
    private ListView expandableListView;
    private String cid, cname;
    private ReceivingAdapter receivingAdapter;
    private List<Demand> demandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_server);
        gettitle();
        initHeader(cname);

        initView();

        LocationUtils.getInstance(this).startLoaction(this);


    }

    public void getdemand(double user_lat, double user_lon) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_DEMAND);
        map.put("category_id", cid);
        map.put("user_lat", user_lat + "");
        map.put("user_lon", user_lon + "");
        map.put("server_type", "1");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        refreshlistview = (PullToRefreshListView) this.findViewById(R.id.refreshlistview);
//        refreshlistview.setbottom(DensityUtil.dip2px(15));
        expandableListView = refreshlistview.getRefreshableView();

    }

    public void gettitle() {
        cid = getIntent().getStringExtra("cid");
        cname = getIntent().getStringExtra("cname");
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_DEMAND.equals(method)) {
            demandList = JSON.parseArray(json.getString("data"), Demand.class);
            receivingAdapter = new ReceivingAdapter(getContext(), demandList, this);
            expandableListView.setAdapter(receivingAdapter);
        }
    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        getdemand(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Intent intent = new Intent(getContext(), AtWillBuyActivity.class);
        intent.putExtra("xid", demandList.get(position).getId());
        intent.putExtra("title", demandList.get(position).getCat_name());
        intent.putExtra("category_id", demandList.get(position).getCategory_id());
        intent.putExtra("avatar", demandList.get(position).getUser_photo());
        startActivity(intent);
    }
}
