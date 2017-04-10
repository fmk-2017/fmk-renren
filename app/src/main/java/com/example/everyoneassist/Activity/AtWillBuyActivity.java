package com.example.everyoneassist.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.EvaluateAdapter;
import com.example.everyoneassist.Entity.OrderBean;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.View.MyListView;
import com.example.everyoneassist.View.MyListView2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AtWillBuyActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private final String DEMAND_INFO = "demand_info";
    private String demand_id;
    private MyListView2 mylistview;
    private OrderBean orderBean;
    private EvaluateAdapter mEvaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_will_buy);
        initHeader("随意购");
        demand_id = getIntent().getStringExtra("xid");
        initView();
        getInfo();
    }

    private void getInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", DEMAND_INFO);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", demand_id);
        HttpPostRequestUtils.getInstance(this).Post(map);

        mEvaAdapter = new EvaluateAdapter(AtWillBuyActivity.this);
        mylistview.setAdapter(mEvaAdapter);

    }

    private void initView() {
        mylistview = (MyListView2) this.findViewById(R.id.lvMy);


    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (DEMAND_INFO.equals(method)) {
            orderBean = JSON.parseObject(json.getJSONObject("data").getString("order_info"), OrderBean.class);

        }
    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
