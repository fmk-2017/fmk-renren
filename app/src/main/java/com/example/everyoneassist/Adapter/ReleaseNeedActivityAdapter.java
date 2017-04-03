package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Activity.ReleaseNeedActivity;
import com.example.everyoneassist.Entity.Need_Cat;
import com.example.everyoneassist.R;
import com.example.everyoneassist.View.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengm on 2017-3-20.
 */

public class ReleaseNeedActivityAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

    /**
     * 用了存放点击的gridview的item的pos和这个pos的选中状态
     */
    public Map<Integer, Boolean> gvChooseMap = new HashMap<Integer, Boolean>();
    public List<String> list = new ArrayList<>();
    private Context context;
    private List<Need_Cat> need_catList;

    public ReleaseNeedActivityAdapter(Context context, List<Need_Cat> need_catList) {
        this.context = context;
        this.need_catList = need_catList;
    }

    @Override
    public int getCount() {
        return need_catList.size();
    }

    @Override
    public Object getItem(int position) {
        return need_catList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.releaseneedactivityadapter_item, null, false);
            vh = new ViewHolder();
            vh.server_title = (TextView) convertView.findViewById(R.id.server_title);
            vh.server_gridview = (MyGridView) convertView.findViewById(R.id.server_gridview);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        vh.server_title.setText(need_catList.get(position).getName());

        String[] str = new String[need_catList.get(position).getItem().size()];
        need_catList.get(position).getItem().toArray(str);
        vh.server_gridview.setTag(position);
        vh.server_gridview.setAdapter(new TextItemAdapter(context, str));
        vh.server_gridview.setOnItemClickListener(this);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int pos = (int) parent.getTag();
        String item = need_catList.get(pos).getItem().get(position);
        if (view.isActivated()) {
            list.add(item);
        } else {
            list.remove(item);
        }
    }

    class ViewHolder {
        TextView server_title;
        MyGridView server_gridview;
    }

}
