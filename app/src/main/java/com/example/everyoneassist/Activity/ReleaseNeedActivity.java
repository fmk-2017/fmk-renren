package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.example.everyoneassist.Adapter.ReleaseNeedActivityAdapter;
import com.example.everyoneassist.Adapter.TextItemAdapter;
import com.example.everyoneassist.Entity.Need_Cat;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.LocationUtils;
import com.example.everyoneassist.View.MyGridView;
import com.example.everyoneassist.View.MyListView;
import com.example.everyoneassist.View.MyListView2;

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
    private EditText skill_content;
    private TextView skill_type, def_time, release;
    private MyListView2 server_Item;
    private View view1;
    private MyGridView setver_time_gridview, sex_gridview, server_type_gridview;
    private String strs = "";
    private HashMap<String, String> map = new HashMap<String, String>();
    private List<Need_Cat> need_catList;
    private String cat_id;
    private ReleaseNeedActivityAdapter releaseneed;
    private String server_type, server_sex, server_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_need);
        initHeader("发布需求");

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        initView();

        LocationUtils.getInstance(this).startLoaction(this);

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

        def_time = (TextView) this.findViewById(R.id.def_time);
        def_time.setOnClickListener(this);

        skill_content = (EditText) this.findViewById(R.id.skill_content);

        server_Item = (MyListView2) this.findViewById(R.id.server_Item);
        view1 = this.findViewById(R.id.view1);

        setver_time_gridview = (MyGridView) this.findViewById(R.id.setver_time_gridview);
        sex_gridview = (MyGridView) this.findViewById(R.id.sex_gridview);
        server_type_gridview = (MyGridView) this.findViewById(R.id.server_type_gridview);

        String[] times = getResources().getStringArray(R.array.service_validity);
        setver_time_gridview.setAdapter(new TextItemAdapter(this, times));
        String[] sexs = getResources().getStringArray(R.array.server_sex);
        sex_gridview.setAdapter(new TextItemAdapter(this, sexs));
        String[] types = getResources().getStringArray(R.array.service_type);
        server_type_gridview.setAdapter(new TextItemAdapter(this, types));

        server_type_gridview.setOnItemClickListener(this);
        sex_gridview.setOnItemClickListener(this);
        setver_time_gridview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skill_type:
                Intent intent = new Intent(this, ReleaseNeedActivity.class);
                intent.putExtra("start", "1");
                startActivityForResult(intent, REQUEST_NEEDTYPE_CODE);
                break;
            case R.id.def_time:

                break;
            case R.id.release:
                String info = skill_content.getText().toString().trim();
                if (TextUtils.isEmpty(server_day) || TextUtils.isEmpty(server_sex) || TextUtils.isEmpty(server_type) || TextUtils.isEmpty(info)) {
                    Toast.makeText(getApplicationContext(), "所有选项都为必填", Toast.LENGTH_SHORT).show();
                }
                map.put("act", METHOD_ADD_DEMEND);
                map.put("user_id", shared.getString("user_id", ""));
                map.put("info", info);
                if (releaseneed != null) {
                    List<String> list = releaseneed.list;
                    for (String str : list)
                        strs += "," + str;
                    map.put("server_tag", strs.substring(1));
                }
                map.put("category_id", id);
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view.isSelected()) view.setSelected(false);
        else view.setSelected(true);
        if (parent.getId() == R.id.server_type_gridview) {
            server_type = position + "";
        } else if (parent.getId() == R.id.sex_gridview) {
            server_sex = position + "";
        } else if (parent.getId() == R.id.setver_time_gridview) {
            server_day = position + "";
        }

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        map.put("user_lat", aMapLocation.getLatitude() + "");
        map.put("user_lon", aMapLocation.getLongitude() + "");
    }
}
