package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.OrderAdapter;
import com.example.everyoneassist.Entity.OrderBean;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback,AdapterView.OnItemClickListener {

    private final String MY_DEMAND = "Mydemand";

    private TextView buyer, servant;
    //    private TextView textview1, textview2, textview3, textview4;
    private TextView[] textviews = new TextView[4];
    private ListView order_listview;

    private List<OrderBean> orderBeanList;
    private OrderAdapter mAdapter;

    private String server_status = "0";
    private String server_type = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initHeader("我的订单");

        initView();
        getOrder(server_type, server_status);
    }

    private void getOrder(String type, String status) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", MY_DEMAND);
        map.put("type_status", type);
        map.put("user_id", shared.getString("user_id",""));
        map.put("page", "1");
        map.put("status", status);
        HttpPostRequestUtils.getInstance(MyOrderActivity.this).Post(map);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (orderBeanList != null && orderBeanList.size() > 0){
            OrderBean bean = orderBeanList.get(position);
            Intent intent = new Intent(MyOrderActivity.this,AtWillBuyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean",bean);
            startActivity(intent);
        }
    }

    private void initView() {
        buyer = (TextView) this.findViewById(R.id.buyer);
        buyer.setSelected(true);
        servant = (TextView) this.findViewById(R.id.servant);
        servant.setOnClickListener(this);
        buyer.setOnClickListener(this);
        textviews[0] = (TextView) this.findViewById(R.id.textview1);
        textviews[0].setSelected(true);
        textviews[1] = (TextView) this.findViewById(R.id.textview2);
        textviews[2] = (TextView) this.findViewById(R.id.textview3);
        textviews[3] = (TextView) this.findViewById(R.id.textview4);

        for (TextView textview : textviews)
            textview.setOnClickListener(this);

        order_listview = (ListView) this.findViewById(R.id.order_listview);
        order_listview.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyer:
                buyer.setSelected(true);
                servant.setSelected(false);
                getOrder(server_type, server_status);
                break;
            case R.id.servant:
                buyer.setSelected(false);
                servant.setSelected(true);
                getOrder(server_type, server_status);
                break;
            case R.id.textview1:
                select(0);
                break;
            case R.id.textview2:
                select(1);
                break;
            case R.id.textview3:
                select(2);
                break;
            case R.id.textview4:
                select(3);
                break;
        }
    }

    public void select(int index) {
        for (TextView textview : textviews)
            textview.setSelected(false);
        textviews[index].setSelected(true);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        Log.e("tag",json.toString());
        orderBeanList = JSON.parseArray(json.getString("data"),OrderBean.class);
        order_listview.setAdapter(new OrderAdapter(MyOrderActivity.this,orderBeanList));
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("tag",method+"---"+error);

    }

    @Override
    public Context getContext() {
        return this;
    }
}
