package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Activity.EditSkillActivity;
import com.example.everyoneassist.Activity.SkillManagerActivity;
import com.example.everyoneassist.Entity.Skill;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.MyHorizontalScrollView;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengm on 2017/1/13.
 */

public class SkillAdapter extends BaseAdapter implements View.OnClickListener {

    private final String METHOD_DEL_SERVER = "del_server";  //删除技能
    private final String METHOD_OFF_SERVER = "off_server";  //关闭技能
    private Context context;
    private HttpPostRequestUtils.HttpPostRequestCallback hprc;
    private List<Skill> skills;

    public SkillAdapter(HttpPostRequestUtils.HttpPostRequestCallback hprc) {
        this.context = hprc.getContext();
        this.hprc = hprc;
    }

    public SkillAdapter(HttpPostRequestUtils.HttpPostRequestCallback hprc, List<Skill> skills) {
        this.context = hprc.getContext();
        this.hprc = hprc;
        this.skills = skills;
    }

    @Override
    public int getCount() {
        return skills.size();
    }

    @Override
    public Object getItem(int position) {
        return skills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            if (context instanceof SkillManagerActivity)
                convertView = LayoutInflater.from(context).inflate(R.layout.skillmanageradapter_item, null, false);
            else
                convertView = LayoutInflater.from(context).inflate(R.layout.skilladapter_item, null, false);
            viewholder = new ViewHolder();
            viewholder.amend_skill = (TextView) convertView.findViewById(R.id.amend_skill);
            viewholder.close_skill = (TextView) convertView.findViewById(R.id.close_skill);
            viewholder.delete_skill = (TextView) convertView.findViewById(R.id.delete_skill);
            viewholder.imagelist = (MyHorizontalScrollView) convertView.findViewById(R.id.imagelist);
            viewholder.skill_title = (TextView) convertView.findViewById(R.id.skill_title);
            viewholder.skill_status = (TextView) convertView.findViewById(R.id.skill_status);
            viewholder.skill_lx = (TextView) convertView.findViewById(R.id.skill_lx);
            viewholder.skill_type = (TextView) convertView.findViewById(R.id.skill_type);
            viewholder.skill_price = (TextView) convertView.findViewById(R.id.skill_price);
            viewholder.skill_time = (TextView) convertView.findViewById(R.id.skill_time);
            viewholder.skill_content = (TextView) convertView.findViewById(R.id.skill_content);
            convertView.setTag(viewholder);
        } else viewholder = (ViewHolder) convertView.getTag();

        Skill skill = skills.get(position);
        viewholder.skill_title.setText(skill.getServer_name());
        if ("0".equals(skill.getStatus())) viewholder.skill_status.setText("等待审核");
        else viewholder.skill_status.setText("审核通过");
        viewholder.skill_lx.setText(skill.getServer_name());
        viewholder.skill_type.setText(skill.getServer_name());

        viewholder.skill_price.setText(NumberFormat.getInstance().format(Double.valueOf(skill.getSkill_price())) + "元");

        String[] weeks = context.getResources().getStringArray(R.array.service_time);
        viewholder.skill_time.setText(weeks[Integer.valueOf(skill.getServer_time())]);
        viewholder.skill_content.setText(skill.getSkill_info());

        viewholder.amend_skill.setTag(skill.getSkill_id());
        viewholder.close_skill.setTag(skill.getSkill_id());
        viewholder.delete_skill.setTag(skill.getSkill_id());
        viewholder.amend_skill.setOnClickListener(this);
        viewholder.close_skill.setOnClickListener(this);
        viewholder.delete_skill.setOnClickListener(this);
        viewholder.imagelist.setVisibility(View.GONE);

        viewholder.imagelist.setAdapter(new SkillImageAdapter(context, 2, skill.getSkill_photos()));

        return convertView;
    }

    @Override
    public void onClick(View v) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (v.getId()) {
            case R.id.amend_skill:
                Intent intent = new Intent(context, EditSkillActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("skill_type", 1);
                intent.putExtra("skill_id", (String) v.getTag());
                context.startActivity(intent);
                break;
            case R.id.close_skill:
                map.put("act", METHOD_OFF_SERVER);
                map.put("user_id", context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", ""));
                map.put("skill_id", (String) v.getTag());
                HttpPostRequestUtils.getInstance(hprc).Post(map);
                break;
            case R.id.delete_skill:
                map.put("act", METHOD_DEL_SERVER);
                map.put("user_id", context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_id", ""));
                map.put("skill_id", (String) v.getTag());
                HttpPostRequestUtils.getInstance(hprc).Post(map);
                break;
        }
    }

    class ViewHolder {
        TextView amend_skill, close_skill, delete_skill, skill_title, skill_status, skill_lx, skill_type, skill_price, skill_time, skill_content;
        MyHorizontalScrollView imagelist;
    }

}
