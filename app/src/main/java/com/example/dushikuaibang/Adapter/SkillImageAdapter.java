package com.example.dushikuaibang.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dushikuaibang.R;

import java.util.List;

/**
 * Created by fengm on 2017-3-4.
 */

public class SkillImageAdapter extends BaseAdapter {

    private Context context;
    private int type = 0;
    private List<String> imagefilelist;

    public SkillImageAdapter(Context context, int type, List<String> imagefilelist) {
        this.context = context;
        this.type = type;
        this.imagefilelist = imagefilelist;
    }

    @Override
    public int getCount() {
        return imagefilelist.size();
    }

    @Override
    public Object getItem(int position) {
        return imagefilelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.skillimageadapter_item, null, false);
            vholder = new ViewHolder();
            vholder.skill_image = (ImageView) convertView.findViewById(R.id.skill_image);
            convertView.setTag(vholder);
        } else vholder = (ViewHolder) convertView.getTag();

        String imagepath = imagefilelist.get(position);
        if ((imagefilelist.size() - 1) == position) {
            if (!"add".equals(imagepath))
                vholder.skill_image.setImageBitmap(BitmapFactory.decodeFile(imagepath));
            else vholder.skill_image.setImageResource(R.mipmap.increase_skills_15);
        } else vholder.skill_image.setImageBitmap(BitmapFactory.decodeFile(imagepath));
        return convertView;
    }

    class ViewHolder {
        ImageView skill_image;
    }

}
