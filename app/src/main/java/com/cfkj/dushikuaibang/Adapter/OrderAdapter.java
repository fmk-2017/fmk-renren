package com.cfkj.dushikuaibang.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfkj.dushikuaibang.Entity.OrderBean;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.TimeUtils;
import com.cfkj.dushikuaibang.View.CircleImageView;

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
            holder.oderInfo = (TextView) convertView.findViewById(R.id.oderInfo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("0".equals(orderBeanList.get(position).getStatus()))
            holder.oderInfo.setText(String.format("已有%s人抢单", orderBeanList.get(position).getCount()));
        else if ("1".equals(orderBeanList.get(position).getStatus()))
            holder.oderInfo.setText("正在服务");
        else if ("2".equals(orderBeanList.get(position).getStatus()))
            holder.oderInfo.setText("等待确认");
        else if ("3".equals(orderBeanList.get(position).getStatus()))
            holder.oderInfo.setText("等待评价");
        else if ("4".equals(orderBeanList.get(position).getStatus()))
            holder.oderInfo.setText("服务已完成");
        holder.oderTitName.setText(orderBeanList.get(position).getCat_name());
        holder.oderName.setText(TextUtils.isEmpty(orderBeanList.get(position).getServer_type_name()) ? "无" : orderBeanList.get(position).getServer_type_name());
        if (!TextUtils.isEmpty(orderBeanList.get(position).getServer_price())) {
            holder.oderTip.setText("服务费" + orderBeanList.get(position).getServer_price() + "元");
            holder.oderTip.setVisibility(View.VISIBLE);
        } else holder.oderTip.setVisibility(View.INVISIBLE);
        holder.oderSendTime.setText("送达时间：" + TimeUtils.getFormatTime(orderBeanList.get(position).getAddtime()));
        holder.oderAdd.setText("收货地址：" + (TextUtils.isEmpty(orderBeanList.get(position).getAddress()) ? "无" : orderBeanList.get(position).getAddress()));

        return convertView;
    }

    class ViewHolder {
        CircleImageView oderImg;
        TextView oderTitle, oderInfo, oderTitName, oderName, oderTip, oderSendTime, oderAdd;
    }

}
