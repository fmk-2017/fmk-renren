package com.example.everyoneassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.everyoneassist.R;

import java.util.HashMap;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView buyer, servant;
    //    private TextView textview1, textview2, textview3, textview4;
    private TextView[] textviews = new TextView[4];
    private ListView order_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initHeader("我的订单");


        initView();

        getOrder();

    }

    private void getOrder() {
        HashMap<String, String> map = new HashMap<>();
        map.put("", "");
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyer:
                buyer.setSelected(true);
                servant.setSelected(false);
                break;
            case R.id.servant:
                buyer.setSelected(false);
                servant.setSelected(true);
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

}
