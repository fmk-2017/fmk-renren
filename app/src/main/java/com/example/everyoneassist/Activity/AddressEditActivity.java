package com.example.everyoneassist.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.everyoneassist.R;

public class AddressEditActivity extends BaseActivity {

    private int type;
    private String titles;

    private String name = "", phone = "", addr1 = "", addr2 = "", addr3 = "", address = "";
    private EditText editaddr_name, editaddr_phone, editaddr_detail;
    private TextView editaddr_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) titles = "新增地址";
        else {
            titles = "编辑地址";
            name = getIntent().getStringExtra("name");
            phone = getIntent().getStringExtra("phone");
            address = getIntent().getStringExtra("address");
            addr1 = getIntent().getStringExtra("addr1");
            addr2 = getIntent().getStringExtra("addr2");
            addr3 = getIntent().getStringExtra("addr3");
        }

        initHeader(titles);

        initView();
    }

    private void initView() {
        editaddr_name = (EditText) this.findViewById(R.id.editaddr_name);
        editaddr_phone = (EditText) this.findViewById(R.id.editaddr_phone);
        editaddr_detail = (EditText) this.findViewById(R.id.editaddr_detail);
        editaddr_address = (TextView) this.findViewById(R.id.editaddr_address);

        editaddr_name.setText(name);
        editaddr_phone.setText(phone);
        editaddr_detail.setText(address);
        editaddr_address.setText(addr1 + addr2 + addr3);
    }

}
