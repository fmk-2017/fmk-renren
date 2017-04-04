package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.everyoneassist.Activity.MyOrderActivity;
import com.example.everyoneassist.R;

/**
 * Created by fengm on 2017-4-4.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
//        if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.orderadapter_item, null, false);
//        }
        return convertView;
    }


}
