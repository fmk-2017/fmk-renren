package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.Constant;

public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView addr_manager, update_version, about,tvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initHeader("系统设置");


        initView();
    }

    private void initView() {
        addr_manager = (TextView) this.findViewById(R.id.addr_manager);
        update_version = (TextView) this.findViewById(R.id.update_version);
        about = (TextView) this.findViewById(R.id.about);
        tvExit = (TextView) this.findViewById(R.id.tvExit);
        addr_manager.setOnClickListener(this);
        tvExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addr_manager://地址管理
                startActivity(new Intent(this, AddressManagerActivity.class));
                break;
            case R.id.tvExit://退出
                SharedPreferences shared = getSharedPreferences(Constant.SHARED_NAME, MODE_PRIVATE);
                shared.edit().clear().commit();
                startActivity(new Intent(SystemSettingActivity.this,LoginActivity.class));
                finish();
                break;
        }
    }
}
