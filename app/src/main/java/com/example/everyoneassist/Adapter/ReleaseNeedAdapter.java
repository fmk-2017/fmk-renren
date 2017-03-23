package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Activity.ReleaseNeedTypeActivity;
import com.example.everyoneassist.Entity.HomeCategory;
import com.example.everyoneassist.Entity.Need_Cat;
import com.example.everyoneassist.R;
import com.example.everyoneassist.View.MyGridView;

import java.util.List;

/**
 * Created by fengm on 2017/1/13.
 */
public class ReleaseNeedAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    int colirred = 225;
    int colirgreen = 25;
    int colirblue = 25;
    private Context context;
    private OnItemClickListener itemclick;
    private List<HomeCategory> homeCategories;

    public ReleaseNeedAdapter(OnItemClickListener itemClickListener, List<HomeCategory> homeCategories) {
        this.itemclick = itemClickListener;
        this.context = itemclick.getContext();
        this.homeCategories = homeCategories;
    }

    @Override
    public int getCount() {
        if (homeCategories == null) return 0;
        return homeCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return homeCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.releaseneedadapter_item, null, false);
            viewholder = new ViewHolder();
            viewholder.need_title = (TextView) convertView.findViewById(R.id.need_title);
            viewholder.need_gridview = (MyGridView) convertView.findViewById(R.id.need_gridview);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        HomeCategory Cat = homeCategories.get(position);

//        viewholder.need_title.setBackgroundColor(Color.argb(255, colirred, colirgreen += (float)((200f / homeCategories.size()) * position), colirblue += (float)((200f / homeCategories.size()) * position)));
        viewholder.need_title.setText(Cat.getCat_name());

        viewholder.need_gridview.setAdapter(new HeaderGridViewAdapter(context, Cat.getChild()));
        viewholder.need_gridview.setTag(position);
        viewholder.need_gridview.setOnItemClickListener(this);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = (int) parent.getTag();
        itemclick.ItemClick(view, position, pos);
    }

    public interface OnItemClickListener {
        void ItemClick(View view, int position, long id);

        Context getContext();
    }

    class ViewHolder {
        TextView need_title;
        MyGridView need_gridview;
    }

}
