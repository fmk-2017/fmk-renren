package com.cfkj.dushikuaibang.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfkj.dushikuaibang.Entity.HomeCategory;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.View.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengm on 2017/1/13.
 */
public class AddSkillListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    int colirred = 225;
    int colirgreen = 25;
    int colirblue = 25;
    private Context context;
    private OnItemClickListener itemclick;
    private List<HomeCategory> homeCategories;


    private int[] colors = new int[]{R.color.need_type1,
            R.color.need_type2,
            R.color.need_type3,
            R.color.need_type4,
            R.color.need_type5,
            R.color.need_type6,
            R.color.need_type7,
            R.color.need_type8,
            R.color.need_type9,
            R.color.need_type10,
            R.color.need_type11};


    private int[] image = new int[]{
            R.mipmap.need_type1,
            R.mipmap.need_type2,
            R.mipmap.need_type3,
            R.mipmap.need_type4,
            R.mipmap.need_type5,
            R.mipmap.need_type6,
            R.mipmap.need_type7,
            R.mipmap.need_type8,
            R.mipmap.need_type9,
            R.mipmap.need_type10,
            R.mipmap.need_type11
    };


    public AddSkillListAdapter(OnItemClickListener itemClickListener, List<HomeCategory> homeCategories) {
        this.itemclick = itemClickListener;
        this.context = itemclick.getContext();
        this.homeCategories = homeCategories;

        if (homeCategories == null) this.homeCategories = new ArrayList<HomeCategory>();

        for (int i = 0; i < this.homeCategories.size(); i++) {
            if (this.homeCategories.get(i).getChild() == null || this.homeCategories.get(i).getChild().size() == 0) {
                this.homeCategories.remove(i);
                --i;
            }
        }

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
//        viewholder.need_title.setBackgroundColor(context.getResources().getColor(colors[position % 10]));
//        viewholder.need_title.setBackgroundColor(Color.argb(255, colirred, colirgreen += (float)((200f / homeCategories.size()) * position), colirblue += (float)((200f / homeCategories.size()) * position)));
        viewholder.need_title.setText(Cat.getCat_name());

        Drawable drawable = context.getResources().getDrawable(image[position % 11]);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        viewholder.need_title.setCompoundDrawables(drawable, null, null, null);
        viewholder.need_title.setCompoundDrawablePadding(25);

        HeaderGridViewAdapter hgva = new HeaderGridViewAdapter(context, Cat.getChild());
        hgva.setColor(colors[position % 11]);
        viewholder.need_gridview.setAdapter(hgva);
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
