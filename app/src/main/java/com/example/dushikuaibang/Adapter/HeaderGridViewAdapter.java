package com.example.dushikuaibang.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dushikuaibang.Entity.HomeCategory;
import com.example.dushikuaibang.R;

import java.util.List;

/**
 * Created by fengm on 2017/1/12.
 */

public class HeaderGridViewAdapter extends BaseAdapter {

    private String[] item;
    private Context context;
    private int max = 0;
    private int color = 0;
    private boolean is_image = false;

    private int image[] = new int[]{
            R.mipmap.home_kaisuo,
            R.mipmap.home_banjia,
            R.mipmap.home_jiadianweixiu,
            R.mipmap.home_baojie,
            R.mipmap.home_shutong,
            R.mipmap.home_gouwu,
            R.mipmap.home_kuaidi,
            R.mipmap.home_all
    };

    public HeaderGridViewAdapter(Context context, List<HomeCategory> items, int max, boolean is_image) {
        this.context = context;
        this.max = max;
        this.is_image = is_image;
        if (items == null) {
            this.item = context.getResources().getStringArray(R.array.need_item);
            return;
        }
        this.item = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            this.item[i] = items.get(i).getCat_name();
        }
    }

    public HeaderGridViewAdapter(Context context, List<HomeCategory> items, int max) {
        this.context = context;
        this.max = max;
        if (items == null) {
            this.item = context.getResources().getStringArray(R.array.need_item);
            return;
        }
        this.item = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            this.item[i] = items.get(i).getCat_name();
        }
    }

    public HeaderGridViewAdapter(Context context, List<HomeCategory> items) {
        this.context = context;
        if (items == null) {
            this.item = context.getResources().getStringArray(R.array.need_item);
            return;
        }
        this.item = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            this.item[i] = items.get(i).getCat_name();
        }
    }

    public HeaderGridViewAdapter(Context context, String[] item) {
        this.context = context;
        this.item = item;
    }


    @Override
    public int getCount() {
        if (max != 0)
            if (item.length > max) return max;
        return item.length;
    }

    @Override
    public Object getItem(int position) {
        return item[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.headergridviewadapter_item, null, false);
        TextView textview = (TextView) view.findViewById(R.id.gridview_text);
        textview.setBackgroundColor(Color.WHITE);
        if (color != 0)
            textview.setTextColor(context.getResources().getColor(color));
        textview.setText(item[position]);
        if (is_image) {
            Drawable rightDrawable = context.getResources().getDrawable(image[position]);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
            textview.setCompoundDrawables(null, rightDrawable, null, null);
            textview.setCompoundDrawablePadding(20);
        }
        return view;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
