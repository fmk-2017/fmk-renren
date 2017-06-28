package com.example.dushikuaibang.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dushikuaibang.Entity.MySkill;
import com.example.dushikuaibang.Interface.MyOnClickListener;
import com.example.dushikuaibang.R;

import java.util.List;

/**
 * Created by fengm on 2017-4-27.
 */

public class InfoAdapter extends BaseAdapter {

    private MyOnClickListener clickListener;
    private List<MySkill> mySkills;

    private String[] weeks;

    public InfoAdapter(MyOnClickListener clickListener, List<MySkill> mySkills) {
        this.clickListener = clickListener;
        this.mySkills = mySkills;
        weeks = clickListener.getContext().getResources().getStringArray(R.array.service_time);
    }

    @Override
    public int getCount() {
        if (mySkills == null) return 0;
        return mySkills.size();
    }

    @Override
    public Object getItem(int position) {
        return mySkills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(clickListener.getContext()).inflate(R.layout.info_list_item, null, false);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.name);
            vh.price_num = (TextView) convertView.findViewById(R.id.price_num);
            vh.setver_type = (TextView) convertView.findViewById(R.id.setver_type);
            vh.server_time = (TextView) convertView.findViewById(R.id.server_time);
            vh.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        MySkill mySkill = mySkills.get(position);

        vh.name.setText(mySkill.getCat_name());
        vh.price_num.setText(mySkill.getSkill_price() + "元/次");
        vh.setver_type.setText("服务方式：" + mySkill.getServer_type_name());
        vh.server_time.setText("服务时间：" + weeks[Integer.valueOf(TextUtils.isEmpty(mySkill.getServer_time()) ? "0" : mySkill.getServer_time())]);
        vh.content.setText(mySkill.getSkill_info());
        return convertView;
    }

    class ViewHolder {
        TextView name, price_num, setver_type, server_time, content;
    }

}
