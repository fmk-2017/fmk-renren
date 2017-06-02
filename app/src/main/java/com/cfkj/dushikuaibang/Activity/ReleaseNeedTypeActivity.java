package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.cfkj.dushikuaibang.Adapter.ReleaseNeedAdapter;
import com.cfkj.dushikuaibang.Entity.HomeCategory;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.DebugLog;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class ReleaseNeedTypeActivity extends BaseActivity implements ReleaseNeedAdapter.OnItemClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private final String MERGOD_CAT = "category";
    private ListView need_listview;
    private ReleaseNeedAdapter releaseNeedAdapter;
    private int start;
    private List<HomeCategory> homeCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_need_type);

        start = getIntent().getIntExtra("start", 0);

        initHeader("发布需求");

        initView();

        getCat();

    }

    private void getCat() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", MERGOD_CAT);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        need_listview = (ListView) this.findViewById(R.id.need_listview);

    }

    @Override
    public void ItemClick(View view, int position, int id) {
        Intent intent = new Intent(this, ReleaseNeedActivity.class);
        intent.putExtra("id", homeCategories.get(id).getChild().get(position).getCat_id());
        intent.putExtra("name", homeCategories.get(id).getChild().get(position).getCat_name());
        if (start == 0) startActivity(intent);
        else setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (MERGOD_CAT.equals(method)) {
            homeCategories = JSON.parseArray(json.getString("data"), HomeCategory.class);
            releaseNeedAdapter = new ReleaseNeedAdapter(this, homeCategories);
            need_listview.setAdapter(releaseNeedAdapter);
        }
    }

    @Override
    public void Fail(String method, String error) {
        DebugLog.d("xxxxxxxxxxxxx", "ssssssssssss");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
