package com.example.everyoneassist.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Entity.Region;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.DialogShowUtils;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AddressEditActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, View.OnClickListener {

    private int type;
    private String titles;

    private String province_id;
    private String city_id;
    private String aera_id;
    private String name = "", phone = "", province = "", city = "", aera = "", address = "";
    private EditText editaddr_name, editaddr_phone, editaddr_detail;
    private TextView editaddr_address, confirm;

    private String address_id;
    //1为选择生，2选择是 3选择去
    private int select_type = 1;
    private List<Region> region;
    private DialogShowUtils dsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) titles = "新增地址";
        else {
            titles = "编辑地址";
            address_id = getIntent().getStringExtra("add_id");
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            address = getIntent().getStringExtra("address");
            province = getIntent().getStringExtra("addr1");
            city = getIntent().getStringExtra("addr2");
            aera = getIntent().getStringExtra("addr3");
            province_id = getIntent().getStringExtra("addr1_id");
            city_id = getIntent().getStringExtra("addr2_id");
            aera_id = getIntent().getStringExtra("addr3_id");
        }
        initHeader(titles);
        initView();
    }

    private void initView() {
        editaddr_name = (EditText) this.findViewById(R.id.editaddr_name);
        editaddr_phone = (EditText) this.findViewById(R.id.editaddr_phone);
        editaddr_detail = (EditText) this.findViewById(R.id.editaddr_detail);
        editaddr_address = (TextView) this.findViewById(R.id.editaddr_address);
        confirm = (TextView) this.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        editaddr_name.setText(name);
        editaddr_phone.setText(phone);
        editaddr_detail.setText(address);
        editaddr_address.setText(province + city + aera);
        editaddr_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_type = 1;
                getAddress("1");
            }
        });
    }

    public void getAddress(String addr_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", "region");
        map.put("pid", addr_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if ("region".equals(method)) {
            region = JSON.parseArray(json.getString("data"), Region.class);
            switch (select_type) {
                case 1:
                    dsu = DialogShowUtils.getInstance(this).ShowAddressSelect(region, this);
                    break;
                case 2:
                    dsu = DialogShowUtils.getInstance(this).ShowAddressSelect(region, this);
                    break;
                case 3:
                    dsu = DialogShowUtils.getInstance(this).ShowAddressSelect(region, this);
                    break;
            }
        } else if ("add_address".equals(method)) {
            Toast.makeText(this, "修改地址成功", Toast.LENGTH_SHORT).show();
            finish();
        } else if ("update_address".equals(method)) {
            Toast.makeText(this, "增加地址成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void Fail(String method, String error) {
        getAddress("1");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                getpid();
                break;
            case R.id.confirm:
                EditAddress();
                break;
        }
    }

    public void EditAddress() {
        address = editaddr_detail.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
            return;
        }
        phone = editaddr_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入联系人手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = editaddr_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入联系人称呼", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        if (type != 0) {
            map.put("act", "update_address");
            map.put("address_id", address_id);
        } else map.put("act", "add_address");
        map.put("user_id", shared.getString("user_id", ""));
        map.put("consignee", name);
        map.put("province", province_id);
        map.put("city", city_id);
        map.put("district", aera_id);
        map.put("address", address);
        map.put("tel", phone);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void getpid() {
        switch (select_type) {
            case 1:
                select_type = 2;
                province_id = region.get(dsu.select_id).getRegion_id();
                province = region.get(dsu.select_id).getRegion_name();
                getAddress(province_id);
                break;
            case 2:
                select_type = 3;
                city_id = region.get(dsu.select_id).getRegion_id();
                city = region.get(dsu.select_id).getRegion_name();
                getAddress(city_id);
                break;
            case 3:
                select_type = 1;
                aera_id = region.get(dsu.select_id).getRegion_id();
                aera = region.get(dsu.select_id).getRegion_name();
                editaddr_address.setText(province + city + aera);
                break;
        }
        dsu.dismiss();
    }


}
