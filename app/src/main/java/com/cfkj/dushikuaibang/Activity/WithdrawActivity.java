package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WithdrawActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private EditText money, ali_account, ali_name;
    private TextView balance;
    private Button withdraw;

    private String balan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initHeader("提现");

        balan = getIntent().getStringExtra("money");

        initView();


    }

    private void initView() {
        money = (EditText) this.findViewById(R.id.money);
        ali_account = (EditText) this.findViewById(R.id.ali_account);
        ali_name = (EditText) this.findViewById(R.id.ali_name);

        balance = (TextView) this.findViewById(R.id.balance);
        withdraw = (Button) this.findViewById(R.id.withdraw);

        balance.setText(String.format("可提现余额：%s元", balan));

        withdraw.setOnClickListener(this);
        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String moneys = money.getText().toString().trim();
                if (Float.valueOf(moneys) > Float.valueOf(balan)) money.setError("输入金额出错");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw:
                String moneys = money.getText().toString().trim();
                String ali_accounts = ali_account.getText().toString().trim();
                String ali_names = ali_name.getText().toString().trim();

                if (TextUtils.isEmpty(moneys)) {
                    Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ali_accounts)) {
                    Toast.makeText(this, "请输入支付宝账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ali_names)) {
                    Toast.makeText(this, "请输入支付宝真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Float.valueOf(moneys) > Float.valueOf(balan)) {
                    Toast.makeText(this, "输入金额大于可提现金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("act", "cash");
                map.put("user_id", shared.getString("user_id", ""));
                map.put("amount", ali_accounts);
                map.put("name", ali_names);
                map.put("amount", moneys);
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if ("cash".equals(method)) {
            Toast.makeText(this, "提现请求成功，正在处理提现...", Toast.LENGTH_SHORT).show();
            finish();
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
