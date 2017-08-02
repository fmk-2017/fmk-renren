package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.dushikuaibang.Adapter.MyCollectAdapter;
import com.example.dushikuaibang.Entity.MySkill;
import com.example.dushikuaibang.Interface.MyOnClickListener;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCollectActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, MyOnClickListener {

    private final String METHOD_COLLECT = "collection_list";
    private ListView collect_list;
    private MyCollectAdapter myCollectAdapter;
    private List<MySkill> mySkills = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        initHeader("我的收藏");

        initView();

        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", METHOD_COLLECT);
        map.put("user_id", shared.getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        collect_list = (ListView) this.findViewById(R.id.collect_list);
        myCollectAdapter = new MyCollectAdapter(this, mySkills);
        collect_list.setAdapter(myCollectAdapter);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_COLLECT.equals(method)) {
            if (mySkills != null) mySkills.clear();
            mySkills.addAll(JSON.parseArray(json.getString("data"), MySkill.class));
            myCollectAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("ssssss", "ssssssssss");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitation: //应邀
                int position = (int) v.getTag();
                Intent intent = new Intent(this, MyInfoActivity.class);
                intent.putExtra("user_id", mySkills.get(position).getUser_id());
                intent.putExtra("skill_id", mySkills.get(position).getSkill_id());
                startActivity(intent);
                break;
        }
    }
}
