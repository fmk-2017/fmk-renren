package com.example.dushikuaibang.Adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dushikuaibang.Entity.MySkill;
import com.example.dushikuaibang.Interface.MyOnClickListener;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;
import com.example.dushikuaibang.Utils.TimeUtils;
import com.example.dushikuaibang.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by fengm on 2017/2/14.
 */
public class MyCollectAdapter extends BaseAdapter {

    private MyOnClickListener clickListener;
    private List<MySkill> mySkills;

    public MyCollectAdapter(MyOnClickListener clickListener, List<MySkill> mySkills) {
        this.clickListener = clickListener;
        this.mySkills = mySkills;
    }

    @Override
    public int getCount() {
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
            convertView = LayoutInflater.from(clickListener.getContext()).inflate(R.layout.mycollectadapter_item, null, false);
            vh = new ViewHolder();
            vh.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            vh.start[0] = (ImageView) convertView.findViewById(R.id.start1);
            vh.start[1] = (ImageView) convertView.findViewById(R.id.start2);
            vh.start[2] = (ImageView) convertView.findViewById(R.id.start3);
            vh.image1[0] = (ImageView) convertView.findViewById(R.id.image1);
            vh.image1[1] = (ImageView) convertView.findViewById(R.id.image2);
            vh.image1[2] = (ImageView) convertView.findViewById(R.id.image3);
            vh.server_name = (TextView) convertView.findViewById(R.id.server_name);
            vh.server_type = (TextView) convertView.findViewById(R.id.server_type);
            vh.server_content = (TextView) convertView.findViewById(R.id.server_content);
            vh.server_price = (TextView) convertView.findViewById(R.id.server_price);
            vh.server_time = (TextView) convertView.findViewById(R.id.server_time);
            vh.invitation = (TextView) convertView.findViewById(R.id.invitation);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        for (int i = 0; i < 3; i++) {
            vh.start[i].setVisibility(View.INVISIBLE);
            vh.image1[i].setVisibility(View.INVISIBLE);
        }

        MySkill mySkill = mySkills.get(position);

        ImageLoader.getInstance().displayImage(mySkill.getUser_photo(), vh.avatar, AppUtils.getOptions());

        int score = Integer.valueOf(TextUtils.isEmpty(mySkill.getScore()) ? "0" : mySkill.getScore());
        for (int i = 0; i < score; i++) {
            vh.start[i].setVisibility(View.VISIBLE);
        }
        String[] imagepath = mySkill.getSkill_photo().split(",");
        for (int i = 0; i < imagepath.length; i++) {
            vh.image1[i].setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(AppUtils.getAvatarPath(imagepath[i]), vh.image1[i], AppUtils.getOptions());
        }
        vh.server_name.setText(mySkill.getCat_name());
        vh.server_type.setText(mySkill.getServer_type_name());
        vh.server_content.setText(mySkill.getSkill_info());
        vh.server_price.setText(String.format("价格：%s次", mySkill.getSkill_price()));
        vh.server_time.setText(TimeUtils.getFormatTime(mySkill.getAddtime()));
        vh.invitation.setOnClickListener(clickListener);
        return convertView;
    }

    class ViewHolder {
        CircleImageView avatar;
        ImageView start[] = new ImageView[3];
        ImageView image1[] = new ImageView[3];
        TextView server_name, server_type, server_content, server_price, server_time, invitation;
    }
}
