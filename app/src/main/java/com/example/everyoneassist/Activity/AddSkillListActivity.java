package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.AddSkillListAdapter;
import com.example.everyoneassist.Adapter.ReleaseNeedAdapter;
import com.example.everyoneassist.Entity.HomeCategory;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.DebugLog;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AddSkillListActivity extends BaseActivity implements AddSkillListAdapter.OnItemClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private final String MERGOD_CAT = "category";
    private ListView need_listview;
    private AddSkillListAdapter releaseNeedAdapter;
    private int start;
    private List<HomeCategory> homeCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill_list);
        start = getIntent().getIntExtra("start", 0);

        initHeader("添加技能");

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
        releaseNeedAdapter = new AddSkillListAdapter(this, null);
        need_listview.setAdapter(releaseNeedAdapter);

    }

    @Override
    public void ItemClick(View view, int position, long id) {
        Intent intent = new Intent(this, ReleaseNeedActivity.class);
        intent.putExtra("id", homeCategories.get((int) id).getChild().get(position).getCat_id());
        intent.putExtra("name", homeCategories.get((int) id).getChild().get(position).getCat_name());
        if (start == 0) startActivity(intent);
        else setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (MERGOD_CAT.equals(method)) {
            homeCategories = JSON.parseArray(json.getString("data"), HomeCategory.class);
            releaseNeedAdapter = new AddSkillListAdapter(this, homeCategories);
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
