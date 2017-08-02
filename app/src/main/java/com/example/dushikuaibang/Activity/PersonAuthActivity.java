package com.example.dushikuaibang.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dushikuaibang.Layout.PercentLinearLayout;
import com.example.dushikuaibang.R;

public class PersonAuthActivity extends BaseActivity implements View.OnClickListener {

    private PercentLinearLayout auth_credit, auth_phone, auth_autonym;

    private TextView phone_auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_auth);
        initHeader("个人认证");

        initView();

    }

    private void initView() {
        auth_autonym = (PercentLinearLayout) this.findViewById(R.id.auth_autonym);
        auth_credit = (PercentLinearLayout) this.findViewById(R.id.auth_credit);
        auth_phone = (PercentLinearLayout) this.findViewById(R.id.auth_phone);

        phone_auto = (TextView) this.findViewById(R.id.phone_auto);
        phone_auto.setText("已认证");
        auth_phone.setSelected(true);

        auth_autonym.setOnClickListener(this);
        auth_phone.setOnClickListener(this);
        auth_credit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.isSelected()) {
            Toast.makeText(this, "已认证", Toast.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()) {
            case R.id.auth_autonym:
                startActivity(new Intent(this, AuthAutonymActivity.class));
                break;
            case R.id.auth_phone:
                startActivity(new Intent(this, AuthPhoneActivity.class));
                break;
            case R.id.auth_credit:
                Toast.makeText(this, "交易成功完成达到50次会自动认证哦！", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
