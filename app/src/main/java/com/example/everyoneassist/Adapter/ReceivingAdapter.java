package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Entity.Demand;
import com.example.everyoneassist.Interface.OnInvitedListener;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by fengm on 2017/1/13.
 */

public class ReceivingAdapter extends BaseAdapter {

    private Context context;
    private List<Demand> demandList;

    private View.OnClickListener onclick;


    public ReceivingAdapter(Context context) {
        this.context = context;
    }

    public ReceivingAdapter(Context context, List<Demand> demandList, View.OnClickListener onclick) {
        this.context = context;
        this.demandList = demandList;
        this.onclick = onclick;

    }

    @Override
    public int getCount() {
        return demandList.size();
    }

    @Override
    public Object getItem(int position) {
        return demandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.receivingadapter_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.listview_title = (TextView) convertView.findViewById(R.id.listview_title);
            viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.bestows_num = (TextView) convertView.findViewById(R.id.bestows_num);
            viewHolder.work = (TextView) convertView.findViewById(R.id.work);
            viewHolder.work_content = (TextView) convertView.findViewById(R.id.work_content);
            viewHolder.work_time = (TextView) convertView.findViewById(R.id.work_time);
            viewHolder.work_address = (TextView) convertView.findViewById(R.id.work_address);
            viewHolder.work_price = (TextView) convertView.findViewById(R.id.work_price);
            viewHolder.receiving = (TextView) convertView.findViewById(R.id.receiving);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        if (position == 0) viewHolder.listview_title.setVisibility(View.VISIBLE);
        else viewHolder.listview_title.setVisibility(View.GONE);

        Demand demand = demandList.get(position);
        ImageLoader.getInstance().displayImage("http://m.szwtdl.cn/" + demand.getUser_photo(), viewHolder.avatar);
        viewHolder.user_name.setText(demand.getNickname());
        viewHolder.bestows_num.setText("");

        viewHolder.work.setText(demand.getServer_type().equals("2") ? "购买商品：" : "" + demand.getCat_name());
        viewHolder.work_content.setText(demand.getInfo());
        viewHolder.work_time.setText("发布时间：" + TimeUtils.getFormatTime(demand.getAddtime()));//这个是送达时间还是发布时间 ...
        if (demand.getServer_type().equals("10")) {
            viewHolder.work_address.setVisibility(View.VISIBLE);
            viewHolder.work_price.setVisibility(View.VISIBLE);
        }
        viewHolder.work_address.setText("收货地址：无");
        viewHolder.work_price.setText(String.format("配送费%s元", demand.getServer_price()));

        viewHolder.receiving.setTag(position);
        viewHolder.receiving.setOnClickListener(onclick);

        return convertView;
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.receiving://立即应邀
//                int position = (int) v.getTag();
//                if (invitedListener != null)
//                    invitedListener.onInvited(position);
//                break;
//        }
//    }
  
    class ViewHolder {
        TextView listview_title, user_name, bestows_num, work, work_content, work_time, work_address, work_price, receiving;
        CircleImageView avatar;
    }
}
