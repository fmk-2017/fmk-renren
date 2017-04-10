package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.everyoneassist.R;

/**
 * Created by Administrator on 2017/4/9.
 */

public class EvaluateAdapter extends BaseAdapter {

    private Context context;

    public EvaluateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.mycollectadapter_item, null, false);
        return convertView;
    }

    class ViewHolder{

    }

}
