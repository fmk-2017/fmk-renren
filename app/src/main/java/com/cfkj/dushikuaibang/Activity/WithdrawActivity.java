package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private EditText money, ali_account, ali_name;
    private TextView balance;
    private Button withdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initHeader("提现");

        initView();


    }

    private void initView() {
        money = (EditText) this.findViewById(R.id.money);
        ali_account = (EditText) this.findViewById(R.id.ali_account);
        ali_name = (EditText) this.findViewById(R.id.ali_name);

        balance = (TextView) this.findViewById(R.id.balance);
        withdraw = (Button) this.findViewById(R.id.withdraw);

        balance.setText(String.format("可提现余额：%d元", 50));

        withdraw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw:
                HashMap<String, String> map = new HashMap<>();
                map.put("act", "cash");
                map.put("user_id", shared.getString("user_id", ""));
                map.put("amount", "cash");
                map.put("amount", "cash");
                map.put("amount", "cash");
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if ("cash".equals(method)) {
            
        }
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("sssss", "ssssssss");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
