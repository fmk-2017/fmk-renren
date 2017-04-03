package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.everyoneassist.Activity.MyWalletActivity;
import com.example.everyoneassist.R;

/**
 * Created by fengm on 2017/2/12.
 */

public class MyWalletUseAdapter extends BaseAdapter {

    private Context context;

    public MyWalletUseAdapter(Context context) {
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
        ViewHolder vholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mywalletuseadapter_item, null, false);
            vholder = new ViewHolder();

            convertView.setTag(vholder);
        } else {
            vholder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {
        ImageView walletuse_img;
        TextView walletuse_name, walletuse_order, walletuse_time, walletuse_money, walletuse_status;
    }

}
