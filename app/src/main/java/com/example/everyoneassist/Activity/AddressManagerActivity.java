package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.AddressListAdapter;
import com.example.everyoneassist.Entity.Address;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.DebugLog;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private ListView addr_list;
    private View footerview;
    private TextView address_add;
    private AddressListAdapter ala;
    private List<Address> addressList;
    private int delete_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        initHeader("管理收货地址");

        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }

    private void getAddress() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", "manage_address");
        map.put("user_id", shared.getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        addr_list = (ListView) this.findViewById(R.id.addr_list);
        footerview = LayoutInflater.from(this).inflate(R.layout.addressmanagerlist_footer, null, false);
        address_add = (TextView) footerview.findViewById(R.id.address_add);
        addr_list.addFooterView(footerview);
        ala = new AddressListAdapter(this, this);
        addr_list.setAdapter(ala);

        address_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        switch (v.getId()) {
            case R.id.address_add:
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.addritem_delete:
                delete_position = (int) v.getTag();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("act", "del_address");
                map.put("user_id", shared.getString("user_id", ""));
                map.put("address_id", addressList.get(delete_position).getAddress_id());
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
            case R.id.addritem_edit:
                Address address = addressList.get((int) v.getTag());
                intent.putExtra("type", 1);
                intent.putExtra("add_id", address.getAddress_id());
                intent.putExtra("name", address.getConsignee());
                intent.putExtra("phone", address.getTel());
                intent.putExtra("address", address.getAddress());
                intent.putExtra("addr1_id", address.getProvince_id());
                intent.putExtra("addr2_id", address.getCity_id());
                intent.putExtra("addr3_id", address.getDistrict_id());
                intent.putExtra("addr1", address.getProvince());
                intent.putExtra("addr2", address.getCity());
                intent.putExtra("addr3", address.getDistrict());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if ("manage_address".equals(method)) {
            addressList = JSON.parseArray(json.getString("data"), Address.class);
            ala.setAddress(addressList);
        } else if ("del_address".equals(method)) {
            ala.delete_item(delete_position);
        }
    }

    @Override
    public void Fail(String method, String error) {
        DebugLog.d(method, error);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
