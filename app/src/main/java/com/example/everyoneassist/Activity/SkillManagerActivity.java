package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.SkillAdapter;
import com.example.everyoneassist.Entity.Skill;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.ScreenUtils;
import com.example.everyoneassist.View.MyListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.DensityUtil;

import java.util.HashMap;
import java.util.List;

public class SkillManagerActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private final String METHOD_SKILL = "skill_list";
    private MyListView skilllistview;
    private SkillAdapter skillAdapter;
    private List<Skill> skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_manager);
        initHeader("技能管理");
        setRightImg(R.mipmap.skills_management_03);
        right_img.setOnClickListener(this);

        initView();

        getSkill();
    }

    private void initView() {
        skilllistview = (MyListView) this.findViewById(R.id.skilllistview);
        skilllistview.setbottom(DensityUtil.dip2px(15));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_img:
                startActivity(new Intent(this, EditSkillActivity.class));
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_SKILL.equals(method)) {
            skills = JSON.parseArray(json.getString("data"), Skill.class);
            skillAdapter = new SkillAdapter(this, skills);
            skilllistview.setAdapter(skillAdapter);
        }


    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    private void getSkill() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", METHOD_SKILL);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("page", "1");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }


}
