package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.dushikuaibang.Adapter.ReleaseNeedActivityAdapter;
import com.example.dushikuaibang.Adapter.TextItemAdapter;
import com.example.dushikuaibang.Entity.Need_Cat;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.DialogShowUtils;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.LocationUtils;
import com.example.dushikuaibang.View.MyGridView;
import com.example.dushikuaibang.View.MyListView2;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleaseNeedActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback, AdapterView.OnItemClickListener, AMapLocationListener {

    private final int REQUEST_NEEDTYPE_CODE = 100;
    private final String METHOD_VIEW = "get_view";
    private final String METHOD_ADD_DEMEND = "add_demand";
    private String id;
    private String name;
    private boolean selet = true;
    private EditText skill_content;
    private TextView skill_type, def_time, release, jies;
    private MyListView2 server_Item;
    private View view1;
    private MyGridView setver_time_gridview, sex_gridview, server_type_gridview;
    private String strs = "";
    private HashMap<String, String> map = new HashMap<String, String>();
    private List<Need_Cat> need_catList;
    private String cat_id;
    private ReleaseNeedActivityAdapter releaseneed;
    private String server_type = "1", server_sex = "0", server_day = "1";
    private String[] times, sexs, types;
    private Map<String, Boolean> timemap = new HashMap<String, Boolean>(), sexmap = new HashMap<String, Boolean>(), typemap = new HashMap<String, Boolean>();

    private String server_id;
    private DialogShowUtils dsu;
    private String pay_type;
    private String user_lat, user_lon;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_need);
        initHeader("发布需求");

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        selet = getIntent().getBooleanExtra("selet", true);

        if (!selet) {
            server_id = getIntent().getStringExtra("skill_id");
        }

        initView();

        skill_type.setEnabled(selet);

        LocationUtils.getInstance(this).startLoaction(this);

        api = WXAPIFactory.createWXAPI(this, "wx463580e9dd2620d1");

        getItem();
    }

    private void getItem() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_VIEW);
        map.put("category_id", id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        skill_type = (TextView) this.findViewById(R.id.skill_type);
        skill_type.setText("技能类型：" + name);
        skill_type.setOnClickListener(this);

        release = (TextView) this.findViewById(R.id.release);
        release.setOnClickListener(this);

        jies = (TextView) this.findViewById(R.id.jies);
        jies.setText(name + "简介：");

        def_time = (TextView) this.findViewById(R.id.def_time);
        def_time.setOnClickListener(this);

        skill_content = (EditText) this.findViewById(R.id.skill_content);

        server_Item = (MyListView2) this.findViewById(R.id.server_Item);
        view1 = this.findViewById(R.id.view1);

        setver_time_gridview = (MyGridView) this.findViewById(R.id.setver_time_gridview);
        sex_gridview = (MyGridView) this.findViewById(R.id.sex_gridview);
        server_type_gridview = (MyGridView) this.findViewById(R.id.server_type_gridview);

        times = getResources().getStringArray(R.array.service_validity);
        resetMap(timemap, times);
        sexs = getResources().getStringArray(R.array.server_sex);
        resetMap(sexmap, sexs);
        types = getResources().getStringArray(R.array.service_type);
        resetMap(typemap, types);

        setver_time_gridview.setAdapter(new TextItemAdapter(this, times, timemap));
        sex_gridview.setAdapter(new TextItemAdapter(this, sexs, sexmap));
        server_type_gridview.setAdapter(new TextItemAdapter(this, types, typemap));

        server_type_gridview.setOnItemClickListener(this);
        sex_gridview.setOnItemClickListener(this);
        setver_time_gridview.setOnItemClickListener(this);
    }

    private void resetMap(Map<String, Boolean> map, String[] array) {
        for (String str : array) {
            map.put(str, false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.server_type_gridview) {
            server_type = position + 1 + "";
            resetMap(typemap, types);
            typemap.put(types[position], true);
            server_type_gridview.setAdapter(new TextItemAdapter(this, types, typemap));
        } else if (parent.getId() == R.id.sex_gridview) {
            server_sex = position + "";
            resetMap(sexmap, sexs);
            sexmap.put(sexs[position], true);
            sex_gridview.setAdapter(new TextItemAdapter(this, sexs, sexmap));
        } else if (parent.getId() == R.id.setver_time_gridview) {
            server_day = position + 1 + "";
            resetMap(timemap, times);
            timemap.put(times[position], true);
            setver_time_gridview.setAdapter(new TextItemAdapter(this, times, timemap));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skill_type:
                Intent intent = new Intent(this, ReleaseNeedTypeActivity.class);
                intent.putExtra("start", 1);
                startActivityForResult(intent, REQUEST_NEEDTYPE_CODE);
                break;
            case R.id.def_time:

                break;
            case R.id.release:
                if (selet) {
                    Release();
                    return;
                }
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.getpaytype_dialog, null, false);
                dsu = DialogShowUtils.getInstance(this).SelectPaytype(view);
                TextView alipay = (TextView) view.findViewById(R.id.alipay);
                TextView weixin = (TextView) view.findViewById(R.id.wxpay);
                TextView cancel = (TextView) view.findViewById(R.id.cancel);
                alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_type = "zhifubao";
                        Release();
                        dsu.dismiss();
                    }
                });
                weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_type = "weixin";
                        Release();
                        dsu.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dsu.dismiss();
                    }
                });

                break;
        }
    }

    public void Release() {
        String info = skill_content.getText().toString().trim();
        if (TextUtils.isEmpty(server_day) || TextUtils.isEmpty(server_sex) || TextUtils.isEmpty(server_type) || TextUtils.isEmpty(info)) {
            Toast.makeText(getApplicationContext(), "所有选项都为必填", Toast.LENGTH_SHORT).show();
        }
        if (!selet) {
            map.put("act", "yuyue");
            map.put("type", server_id);
            map.put("pay_type", pay_type);//#支付类型   zhifubao:支付宝   weixin:微信
            map.put("server_id", server_id);//技能id
        } else map.put("act", METHOD_ADD_DEMEND);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("info", info);
        if (releaseneed != null && releaseneed.list.size() > 0) {
            List<String> list = releaseneed.list;
            for (String str : list)
                strs += "," + str;
            map.put("server_tag", strs.substring(1));
        }
        map.put("category_id", id);
        map.put("server_day", server_day);
        map.put("server_sex", server_sex);
        map.put("user_lat", user_lat);
        map.put("user_lon", user_lon);
        map.put("server_type", server_type);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEEDTYPE_CODE && resultCode == RESULT_OK) {
            skill_type.setText("技能类型：" + data.getStringExtra("name"));
            id = data.getStringExtra("id");
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_VIEW.equals(method)) {
            need_catList = JSON.parseArray(json.getString("data"), Need_Cat.class);
            if (need_catList != null && need_catList.size() > 0) {
                view1.setVisibility(View.VISIBLE);
                server_Item.setVisibility(View.VISIBLE);
                releaseneed = new ReleaseNeedActivityAdapter(this, need_catList);
                server_Item.setAdapter(releaseneed);
            }
            if (need_catList.size() > 0) {
                skill_type.setText("技能类型：" + need_catList.get(0).getCat_name());
                cat_id = need_catList.get(0).getCat_id();
                map.put("category_id", cat_id);
            }
        } else if (METHOD_ADD_DEMEND.equals(method)) {
            finish();
        } else if ("yuyue".equals(method)) {
            if ("zhifubao".equals(pay_type)) payali(json.getString("data"));
            else paywx(json.getJSONObject("data"));
        }
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("xxxxxxxxxxxx", "sssssssssssssssss");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (String.valueOf(aMapLocation.getLatitude()).length() > 12) return;
        user_lat = aMapLocation.getLatitude() + "";
        user_lon = aMapLocation.getLongitude() + "";
    }

    private void payali(final String orderInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ReleaseNeedActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.e("支付宝支付返回", result.toString());
                if ("9000".equals(result.get("resultStatus"))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ReleaseNeedActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

    private void paywx(JSONObject json) throws JSONException {
        PayReq req = new PayReq();
        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = json.getString("package");
        req.sign = json.getString("sign");
        req.extData = "app data"; // optional
        Toast.makeText(getApplicationContext(), "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

}
