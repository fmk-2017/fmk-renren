package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cfkj.dushikuaibang.Adapter.OrderAdapter;
import com.cfkj.dushikuaibang.Entity.OrderBean;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cfkj.dushikuaibang.R.id.textview2;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback, AdapterView.OnItemClickListener {

    private final String MY_DEMAND = "Mydemand";

    private TextView buyer, servant;
    //    private TextView textview1, textview2, textview3, textview4;
    private TextView[] textviews = new TextView[4];
    private ListView order_listview;

    private List<OrderBean> orderBeanList;
    private OrderAdapter mAdapter;

    private String server_status = "";
    private String server_type = "1";

    private int page = 1;
    private boolean canload = true;
    private OrderAdapter orderAdapter;

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
        map.put("user_id", shared.getString("user_id", ""));
        map.put("page", page + "");

        if ("0".equals(status)) {
            map.put("is_pay", "0");
            map.put("status", status);
        } else {
            if (!TextUtils.isEmpty(status)) {
                map.put("status", status);
                map.put("is_pay", "1");
            }
        }
        HttpPostRequestUtils.getInstance(MyOrderActivity.this).Post(map);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (orderBeanList != null && orderBeanList.size() > 0) {
            OrderBean bean = orderBeanList.get(position);
            Intent intent = new Intent(MyOrderActivity.this, AtWillBuyActivity.class);
            intent.putExtra("xid", bean.getId());
            intent.putExtra("category_id", bean.getCat_id());
            intent.putExtra("title", bean.getCat_name());
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
        textviews[1] = (TextView) this.findViewById(textview2);
        textviews[2] = (TextView) this.findViewById(R.id.textview3);
        textviews[3] = (TextView) this.findViewById(R.id.textview4);

        for (TextView textview : textviews)
            textview.setOnClickListener(this);

        order_listview = (ListView) this.findViewById(R.id.order_listview);
        order_listview.setOnItemClickListener(this);
        orderBeanList = new ArrayList<>();
        orderAdapter = new OrderAdapter(MyOrderActivity.this, orderBeanList);
        order_listview.setAdapter(orderAdapter);

        order_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) >= (orderBeanList.size() - 4) && canload) {
                    canload = false;
                    ++page;
                    getOrder(server_type, server_status);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyer:
                buyer.setSelected(true);
                servant.setSelected(false);
                server_type = "1";
                textviews[1].setVisibility(View.VISIBLE);
                select(0);
                server_status = "";
                break;
            case R.id.servant:
                buyer.setSelected(false);
                servant.setSelected(true);
                server_type = "2";
                textviews[1].setVisibility(View.GONE);
                select(0);
                server_status = "";
                break;
            case R.id.textview1:
                select(0);
                server_status = "";
                break;
            case textview2:
                select(1);
                server_status = "0";
                break;
            case R.id.textview3:
                select(2);
                server_status = "1";
                break;
            case R.id.textview4:
                select(3);
                server_status = "2";
                break;
        }
        page = 1;
        getOrder(server_type, server_status);
    }

    public void select(int index) {
        for (TextView textview : textviews)
            textview.setSelected(false);
        textviews[index].setSelected(true);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        Log.e("tag", json.toString());
        if (page == 1 && orderBeanList != null) orderBeanList.clear();
        orderBeanList.addAll(JSON.parseArray(json.getString("data"), OrderBean.class));
        orderAdapter.notifyDataSetChanged();
        canload = true;
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("tag", method + "---" + error);
        if (page > 1)
            --page;
        else orderBeanList.clear();
        canload = false;
        if (orderAdapter != null)
            orderAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
