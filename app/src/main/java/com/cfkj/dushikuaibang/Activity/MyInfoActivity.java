package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cfkj.dushikuaibang.Adapter.InfoAdapter;
import com.cfkj.dushikuaibang.Entity.MySkill;
import com.cfkj.dushikuaibang.Interface.MyOnClickListener;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.Constant;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;
import com.cfkj.dushikuaibang.View.CircleImageView;
import com.cfkj.dushikuaibang.View.MyListView2;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyInfoActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, MyOnClickListener {

    private final String METHOD_SERVER = "list_server";
    private final String METHOD_MERCHANT = "enshrine_merchant";
    private final String METHOD_MESSAGE = "send_message";
    private final int REQUEST_LIUYAN = 123;
    private CircleImageView ivMeHead;
    private TextView tvUserName, num;
    private MyListView2 mylistview2;
    private TextView leave, collect, subscribe, leaves;
    private List<MySkill> mySkills;
    private String skill_id, userid;
    private ImageView[] start = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_my_info);

        skill_id = getIntent().getStringExtra("skill_id");
        userid = getIntent().getStringExtra("user_id");
        initview();

        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_SERVER);
        map.put("userid", userid);
        map.put("skill_id", skill_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initview() {
        ivMeHead = (CircleImageView) findViewById(R.id.ivMeHead);
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        mylistview2 = (MyListView2) findViewById(R.id.mylistview2);

        leave = (TextView) this.findViewById(R.id.leave);
        leaves = (TextView) this.findViewById(R.id.leaves);
        collect = (TextView) this.findViewById(R.id.collect);
        subscribe = (TextView) this.findViewById(R.id.subscribe);
        num = (TextView) this.findViewById(R.id.num);
        num.setOnClickListener(this);

        start[0] = (ImageView) this.findViewById(R.id.start1);
        start[1] = (ImageView) this.findViewById(R.id.start2);
        start[2] = (ImageView) this.findViewById(R.id.start3);

        leaves.setOnClickListener(this);
        leave.setOnClickListener(this);
        collect.setOnClickListener(this);
        subscribe.setOnClickListener(this);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_SERVER.equals(method)) {
            String avatar = json.getJSONObject("data").getJSONObject("user_info").getString("user_photo");
            String name = json.getJSONObject("data").getJSONObject("user_info").getString("user_name");
            String count = json.getJSONObject("data").getJSONObject("user_info").getString("count");
            String score = json.getJSONObject("data").getJSONObject("user_info").getString("score");
            num.setText(String.format("服务评价 （%s）", TextUtils.isEmpty(count) ? "0" : count));
            for (int i = 0; i < Integer.valueOf(score); i++) {
                start[i].setVisibility(View.VISIBLE);
            }
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
        } else if (METHOD_MERCHANT.equals(method)) {
            Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
        } else if (METHOD_MESSAGE.equals(method)) {
            Toast.makeText(getApplicationContext(), "留言成功", Toast.LENGTH_SHORT).show();
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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.leave:
                startActivityForResult(new Intent(this, EditTextActivity.class).putExtra("title", "留言"), REQUEST_LIUYAN);
                break;
            case R.id.collect:
                collect();
                break;
            case R.id.subscribe: //这个跳转对应的是什么技能？

                break;
            case R.id.num:
                intent = new Intent(this, CommentActivity.class);
                intent.putExtra("user_id", userid);
                intent.putExtra("skill_id", skill_id);
                startActivity(intent);
                break;
            case R.id.leaves:
                intent = new Intent(this, CommentActivity.class);
                intent.putExtra("user_id", userid);
                intent.putExtra("skill_id", skill_id);
                startActivity(intent);
                break;
        }
    }


    public void collect() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", METHOD_MERCHANT);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("goods_id", skill_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void leave(String leav) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", METHOD_MESSAGE);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("merchant_id", userid);
        map.put("content", leav);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_LIUYAN) {
            if (TextUtils.isEmpty(data.getStringExtra("content"))) return;
            leave(data.getStringExtra("content"));
        }


    }
}
