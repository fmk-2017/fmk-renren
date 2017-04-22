package com.example.everyoneassist.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.InvitationAdapter;
import com.example.everyoneassist.Entity.Invitation;
import com.example.everyoneassist.Entity.OrderBean;
import com.example.everyoneassist.Entity.Order_Info;
import com.example.everyoneassist.Layout.PercentLinearLayout;
import com.example.everyoneassist.Layout.PercentRelativeLayout;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.AppUtils;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.View.MyListView;
import com.example.everyoneassist.View.MyListView2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AtWillBuyActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, View.OnClickListener {

    private final String DEMAND_INFO = "demand_info";
    private final String APPOINTMENT_SERVER = "user_order_save";
    private final String COMMENT = "sever_evaluate";
    private final String USER_ORDER_ADD = "user_order_add";
    private final String TO_USER_AFFIRM = "to_user_affirm";
    private final String USER_AFFIRM = "user_affirm";
    private String demand_id;
    private MyListView2 mylistview;
    private Order_Info order_info;
    private List<Invitation> invitations;
    private TextView content, time, address_buy, address_receving, price, order_no, contacts, contacts_phone, remark, textView4, release_comment;
    private EditText comment;
    private InvitationAdapter ia;
    private boolean is_self;
    private PercentLinearLayout bottom;

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
    }

    private void initView() {
        mylistview = (MyListView2) this.findViewById(R.id.mylistview2);
        mylistview.setFocusable(false);

        content = (TextView) this.findViewById(R.id.content);
        time = (TextView) this.findViewById(R.id.time);
        address_buy = (TextView) this.findViewById(R.id.address_buy);
        address_receving = (TextView) this.findViewById(R.id.address_receving);
        price = (TextView) this.findViewById(R.id.price);
        order_no = (TextView) this.findViewById(R.id.order_no);
        contacts = (TextView) this.findViewById(R.id.contacts);
        contacts_phone = (TextView) this.findViewById(R.id.contacts_phone);
        remark = (TextView) this.findViewById(R.id.remark);
        textView4 = (TextView) this.findViewById(R.id.textView4);
        release_comment = (TextView) this.findViewById(R.id.release_comment);
        bottom = (PercentLinearLayout) this.findViewById(R.id.bottom);

        textView4.setOnClickListener(this);
        release_comment.setOnClickListener(this);

        comment = (EditText) this.findViewById(R.id.comment);
    }

    public void setviewData() {
        content.setText(order_info.getInfo());
        time.setText(TimeUtils.getFormatTime(order_info.getAddtime()));
//        address_buy.setText();
//        address_buy.setVisibility(View.GONE);
        address_receving.setText(order_info.getAddress());
        price.setText("服务费：" + order_info.getServer_price());
        order_no.setText("订单号：" + order_info.getD_order());
        contacts.setText("联系人：" + order_info.getConsignee());
        contacts_phone.setText("联系电话：" + order_info.getTel());
        remark.setText("备注：" + order_info.getRemark());
        textView4.setEnabled(true);
        String op = "";
        switch (order_info.getStatus()) {
            case "0":
                mylistview.setVisibility(View.VISIBLE);
                op = "立即抢单";
                if (shared.getString("user_id", "").equals(order_info.getUser_id())) {
                    textView4.setEnabled(false);
                    op = "等待抢单";
                }
                break;
            case "1":
                if (shared.getString("user_id", "").equals(order_info.getUser_id())) {
                    textView4.setEnabled(false);
                    op = "任务进行中";
                } else op = "完成任务";
                break;
            case "2":
                if (shared.getString("user_id", "").equals(order_info.getUser_id()))
                    op = "确认完成";
                else {
                    op = "等待确认";
                    textView4.setEnabled(false);
                }
                break;
            case "3":
                op = "订单已完成,待评价";
                textView4.setEnabled(false);
                bottom.setVisibility(View.VISIBLE);
                break;
            case "4":
                textView4.setVisibility(View.GONE);
                bottom.setVisibility(View.VISIBLE);
                release_comment.setVisibility(View.GONE);
                comment.setEnabled(false);
                break;
            default:
                textView4.setVisibility(View.GONE);
                break;
        }
        textView4.setText(op);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (DEMAND_INFO.equals(method)) {
            order_info = JSON.parseObject(json.getJSONObject("data").getString("order_info"), Order_Info.class);
            invitations = JSON.parseArray(json.getJSONObject("data").getString("user_info"), Invitation.class);
            for (Invitation invitation : invitations) {
                if (shared.getString("user_id", "").equals(invitation.getUser_id()))
                    textView4.setEnabled(false);
            }
            setviewData();
            if (invitations != null && invitations.size() > 0) {
                if (order_info.getUser_id().equals(shared.getString("user_id", ""))) is_self = true;
                else is_self = false;
                ia = new InvitationAdapter(this, invitations, this, is_self);
                mylistview.setAdapter(ia);
            } else {
                mylistview.setVisibility(View.GONE);
            }
        } else if (APPOINTMENT_SERVER.equals(method)) {
            Toast.makeText(this, "已选择应邀者", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (USER_AFFIRM.equals(method)) {
            Toast.makeText(this, "确认服务完成", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (TO_USER_AFFIRM.equals(method)) {
            Toast.makeText(this, "服务完成", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (COMMENT.equals(method)) {
            Toast.makeText(this, "已评价", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (USER_ORDER_ADD.equals(method)) {
            Toast.makeText(this, "抢单成功", Toast.LENGTH_SHORT).show();
            getInfo();
        }
    }

    @Override
    public void Fail(String method, String error) {
        Log.e("1111111111111", "222222222222222222");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitantion:
                int position = (int) v.getTag();
                subscribe(invitations.get(position).getUser_id());
                break;
            case R.id.textView4:
                switch (order_info.getStatus()) {
                    case "0":
                        bid(order_info.getId());
                        break;
                    case "1":
                        verify(order_info.getId());
                        break;
                    case "2":
                        consent(order_info.getId());
                        break;
                    case "3":
                        break;
                    default:
                        textView4.setVisibility(View.GONE);
                        break;
                }
                break;
            case R.id.release_comment:
                String comments = comment.getText().toString().trim();
                if (TextUtils.isEmpty(comments)) {
                    Toast.makeText(v.getContext(), "评价内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                comment(comments);
                break;
        }
    }

    public void verify(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", TO_USER_AFFIRM);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void consent(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_AFFIRM);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void bid(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_ORDER_ADD);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void subscribe(String user_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", APPOINTMENT_SERVER);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("to_user_id", user_id);
        map.put("demand_id", order_info.getId());
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    public void comment(String comment) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", COMMENT);
        map.put("score", "3");
        map.put("evaluate", comment);
        map.put("demand_id", order_info.getId());
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

}
