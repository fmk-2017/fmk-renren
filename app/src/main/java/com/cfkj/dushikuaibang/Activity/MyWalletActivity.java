package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cfkj.dushikuaibang.Adapter.MyWalletUseAdapter;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private ListView walletuse_list;
    private View headerView;
    private TextView mywallet_deposit, mywallet_withdraw_deposit, balances;
    private String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        initHeader("我的钱包");

        initView();

        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", "purse");
        map.put("user_id", shared.getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        walletuse_list = (ListView) this.findViewById(R.id.walletuse_list);
        headerView = LayoutInflater.from(this).inflate(R.layout.mywallet_header, null, false);

        walletuse_list.addHeaderView(headerView);
        walletuse_list.setAdapter(new MyWalletUseAdapter(this));

        balances = (TextView) headerView.findViewById(R.id.balance);
        mywallet_deposit = (TextView) headerView.findViewById(R.id.mywallet_deposit);
        mywallet_withdraw_deposit = (TextView) headerView.findViewById(R.id.mywallet_withdraw_deposit);

        mywallet_deposit.setOnClickListener(this);
        mywallet_withdraw_deposit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mywallet_deposit:

                break;
            case R.id.mywallet_withdraw_deposit:
                startActivity(new Intent(v.getContext(), WithdrawActivity.class));
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if ("purse".equals(method)) {
            balance = json.getJSONObject("data").getString("balance");
            balances.setText(balance);
        }
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("sssssssss", "sssssssssss");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
