package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.Activity.AddressEditActivity;
import com.example.everyoneassist.Activity.AddressManagerActivity;
import com.example.everyoneassist.R;

/**
 * Created by fengm on 2017/2/11.
 */

public class AddressListAdapter extends BaseAdapter {

    private Context context;

    public AddressListAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.addresslistadapter_item, null, false);
            vholder = new ViewHolder();
            vholder.addritem_delete = (TextView) convertView.findViewById(R.id.addritem_delete);
            vholder.addritem_edit = (TextView) convertView.findViewById(R.id.addritem_edit);
            vholder.addritem_address = (TextView) convertView.findViewById(R.id.addritem_address);
            vholder.addritem_phone = (TextView) convertView.findViewById(R.id.addritem_phone);
            vholder.addritem_name = (TextView) convertView.findViewById(R.id.addritem_name);
            convertView.setTag(vholder);
        } else {
            vholder = (ViewHolder) convertView.getTag();
        }
        vholder.addritem_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddressEditActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("name", "潘先生");
                intent.putExtra("phone", "13530914291");
                intent.putExtra("address", "义乌小商品批发城4楼");
                intent.putExtra("addr1", "广东省");
                intent.putExtra("addr2", "深圳市");
                intent.putExtra("addr3", "龙岗区");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView addritem_delete, addritem_edit, addritem_address, addritem_phone, addritem_name;
    }

}
