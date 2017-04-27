package com.example.everyoneassist.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.InfoAdapter;
import com.example.everyoneassist.Adapter.MyCollectAdapter;
import com.example.everyoneassist.Entity.MySkill;
import com.example.everyoneassist.Interface.MyOnClickListener;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.Constant;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.View.CircleImageView;
import com.example.everyoneassist.View.MyListView2;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyInfoActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, MyOnClickListener {

    private final String METHOD_SERVER = "list_server";
    private CircleImageView ivMeHead;
    private TextView tvUserName;
    private MyListView2 mylistview2;
    private TextView leave, collect, subscribe;
    private List<MySkill> mySkills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_my_info);

        initview();

        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_SERVER);
        map.put("userid", shared.getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initview() {
        ivMeHead = (CircleImageView) findViewById(R.id.ivMeHead);
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        mylistview2 = (MyListView2) findViewById(R.id.mylistview2);

        leave = (TextView) this.findViewById(R.id.leave);
        collect = (TextView) this.findViewById(R.id.collect);
        subscribe = (TextView) this.findViewById(R.id.subscribe);

        leave.setOnClickListener(this);
        collect.setOnClickListener(this);
        subscribe.setOnClickListener(this);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_SERVER.equals(method)) {
            String avatar = json.getJSONObject("data").getJSONObject("user_info").getString("user_photo");
            String name = json.getJSONObject("data").getJSONObject("user_info").getString("user_name");
            if (!TextUtils.isEmpty(avatar)) {
                ImageLoader.getInstance().displayImage(Constant.HOST + avatar, ivMeHead);
            }
            if (TextUtils.isEmpty(name)) {
                tvUserName.setText("人人帮");
            } else {
                tvUserName.setText(name);
            }
            mySkills = JSON.parseArray(json.getJSONObject("data").getString("skill_info"), MySkill.class);
            mylistview2.setAdapter(new InfoAdapter(this, mySkills));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave:

                break;
            case R.id.collect:

                break;
            case R.id.subscribe: //这个跳转对应的是什么技能？

                break;
        }
    }
}
