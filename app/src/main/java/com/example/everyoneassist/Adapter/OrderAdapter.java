package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Activity.MyOrderActivity;
import com.example.everyoneassist.Entity.OrderBean;
import com.example.everyoneassist.Entity.Pic;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.CircleImageView;

import java.util.List;

/**
 * Created by fengm on 2017-4-4.
 */

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<OrderBean> orderBeanList;

    public OrderAdapter(Context context, List<OrderBean> orderBeanList) {
        this.context = context;
        this.orderBeanList = orderBeanList;
    }

    @Override
    public int getCount() {
        if (orderBeanList == null) return 0;
        return orderBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.orderadapter_item, null, false);
            holder = new ViewHolder();
            holder.oderImg = (CircleImageView) convertView.findViewById(R.id.oderImg);
            holder.oderTitle = (TextView) convertView.findViewById(R.id.oderTitle);
            holder.oderTitName = (TextView) convertView.findViewById(R.id.oderTitName);
            holder.oderName = (TextView) convertView.findViewById(R.id.oderName);
            holder.oderTip = (TextView) convertView.findViewById(R.id.oderTip);
            holder.oderSendTime = (TextView) convertView.findViewById(R.id.oderSendTime);
            holder.oderAdd = (TextView) convertView.findViewById(R.id.oderAdd);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.oderTitName.setText(orderBeanList.get(position).getCat_name());
        holder.oderName.setText(TextUtils.isEmpty(orderBeanList.get(position).getServer_type_name()) ? "无":orderBeanList.get(position).getServer_type_name());
        holder.oderTip.setText("配送费"+orderBeanList.get(position).getServer_price()+"元");
        holder.oderSendTime.setText("送达时间："+ TimeUtils.getFormatTime(orderBeanList.get(position).getAddtime()));
        holder.oderAdd.setText("收货地址："+(TextUtils.isEmpty(orderBeanList.get(position).getAddress()) ? "无":TextUtils.isEmpty(orderBeanList.get(position).getAddress())));

        return convertView;
    }

    class ViewHolder {
        CircleImageView oderImg;
        TextView oderTitle, oderInfo, oderTitName, oderName, oderTip, oderSendTime, oderAdd;
    }

}
