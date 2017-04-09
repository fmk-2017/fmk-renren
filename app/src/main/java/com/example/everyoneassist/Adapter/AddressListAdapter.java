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
import com.example.everyoneassist.Entity.Address;
import com.example.everyoneassist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengm on 2017/2/11.
 */

public class AddressListAdapter extends BaseAdapter {

    private Context context;
    private View.OnClickListener onclick;
    private List<Address> addressList;

    public AddressListAdapter(Context context, View.OnClickListener onclick) {
        this.context = context;
        this.onclick = onclick;
        addressList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        Address address = addressList.get(position);
        String addresss = address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress();
        vholder.addritem_address.setText(addresss);
        vholder.addritem_phone.setText(address.getTel());
        vholder.addritem_name.setText(address.getConsignee());

        vholder.addritem_delete.setTag(position);
        vholder.addritem_delete.setOnClickListener(onclick);
        vholder.addritem_edit.setTag(position);
        vholder.addritem_edit.setOnClickListener(onclick);
        return convertView;
    }

    public void setAddress(List<Address> addressList) {
        this.addressList.clear();
        this.addressList.addAll(addressList);
        notifyDataSetChanged();
    }

    public void delete_item(int delete_position) {
        this.addressList.remove(delete_position);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView addritem_delete, addritem_edit, addritem_address, addritem_phone, addritem_name;
    }

}
