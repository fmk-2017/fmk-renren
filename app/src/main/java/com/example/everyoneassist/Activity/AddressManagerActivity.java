package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.everyoneassist.Adapter.AddressListAdapter;
import com.example.everyoneassist.R;

public class AddressManagerActivity extends BaseActivity implements View.OnClickListener {

    private ListView addr_list;
    private View footerview;
    private TextView address_add;
    private AddressListAdapter ala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        initHeader("管理收货地址");

        initView();
    }

    private void initView() {
        addr_list = (ListView) this.findViewById(R.id.addr_list);
        footerview = LayoutInflater.from(this).inflate(R.layout.addressmanagerlist_footer, null, false);
        address_add = (TextView) footerview.findViewById(R.id.address_add);
        addr_list.addFooterView(footerview);
        ala = new AddressListAdapter(this);
        addr_list.setAdapter(ala);

        address_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_add:
                Intent intent = new Intent(this, AddressEditActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
        }
    }
}
