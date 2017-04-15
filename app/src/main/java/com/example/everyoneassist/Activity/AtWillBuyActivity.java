package com.example.everyoneassist.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.InvitationAdapter;
import com.example.everyoneassist.Entity.Invitation;
import com.example.everyoneassist.Entity.Order_Info;
import com.example.everyoneassist.Interface.OnInvitedListener;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.Utils.ToastUtils;
import com.example.everyoneassist.View.MyListView2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AtWillBuyActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, View.OnClickListener,OnInvitedListener {

    private final String DEMAND_INFO = "demand_info";
    private final String APPOINTMENT_SERVER = "appointment_server";
    private final String USER_DELIVERY = "user_delivery";
    private final String TO_USER_AFFIRM = "to_user_affirm";
    private final String USER_AFFIRM = "user_affirm";
    private String demand_id;
    private MyListView2 mylistview;
    private Order_Info order_info;
    private List<Invitation> invitations;
    private TextView content, time, address_buy, address_receving, price, order_no, contacts, contacts_phone, remark, textView4, release_comment;
    private EditText comment;
    private ScrollView mySc;
    private InvitationAdapter ia;
    private boolean is_self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_will_buy);
        initHeader("随意购");

        demand_id = getIntent().getStringExtra("xid");

        initView();
        getInfo();
        initListener();
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
        mySc = (ScrollView) this.findViewById(R.id.mySc);
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
        comment = (EditText) this.findViewById(R.id.comment);
        textView4.setOnClickListener(this);

        //解决ScrollView自定滑动到EditText位置
        mySc.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        mySc.setFocusable(true);
        mySc.setFocusableInTouchMode(true);
        mySc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
    }

    public void setviewData() {
        content.setText(dataStr(order_info.getInfo()));
        time.setText(TextUtils.isEmpty(order_info.getAddtime())?"暂无数据":TimeUtils.getFormatTime(order_info.getAddtime()));
        address_buy.setText(dataStr(order_info.getAddress()));
        address_receving.setText(dataStr(order_info.getAddress()));
        price.setText("配送费："+(TextUtils.isEmpty(order_info.getServer_price())?"暂无数据":order_info.getServer_price()+"元"));
        order_no.setText("订单号：" + dataStr(order_info.getZipcode()));
        contacts.setText("联系人："+ dataStr(order_info.getConsignee()));
        contacts_phone.setText("联系电话:"+ dataStr(order_info.getTel()));
        remark.setText("备注:"+dataStr(order_info.getServer_type_name()));
        String op = "";
        switch (TextUtils.isEmpty(order_info.getStatus())?"3":order_info.getStatus()) {
            case "0":
                op = "立即抢单";
                break;
            case "2":
                op = "完成任务";
                break;
            case "3":
                op = "确认完成";
                break;
            default:
                textView4.setVisibility(View.GONE);
                break;
        }
        textView4.setText(op);
    }

    private String dataStr(String str){
        return TextUtils.isEmpty(str) ? "暂无数据" : str;
    }

    private void initListener(){
        textView4.setOnClickListener(this);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (DEMAND_INFO.equals(method)) {
            order_info = JSON.parseObject(json.getJSONObject("data").toString(), Order_Info.class);
            setviewData();
//            invitations = JSON.parseArray(json.getJSONObject("data").getString("user_info"), Invitation.class);
//            if (order_info.getUser_id().equals(shared.getString("user_id", ""))) is_self = true;
//            else is_self = false;
            //ia = new InvitationAdapter(this, invitations, this, is_self);
            ia = new InvitationAdapter(AtWillBuyActivity.this,this);
            mylistview.setAdapter(ia);
        } else if (APPOINTMENT_SERVER.equals(method)) {
            String result = json.getString("result");
            if (result.equals("success")){
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }else {
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }
            //getInfo();
        } else if (USER_AFFIRM.equals(method)) {//立即抢单
            String result = json.getString("result");
            if (result.equals("success")){
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }else {
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }
            //getInfo();
        } else if (TO_USER_AFFIRM.equals(method)) {
            String result = json.getString("result");
            if (result.equals("success")){
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }else {
                ToastUtils.tipShort(AtWillBuyActivity.this,json.getString("info"));
            }
            //getInfo();
        }
    }

    @Override
    public void Fail(String method, String error) {
        ToastUtils.tipShort(AtWillBuyActivity.this,error);
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
                switch ("2") {//order_info.getStatus()
                    case "0"://立即抢单
                        bid(order_info.getId());
                        break;
                    case "2"://完成任务
                        verify(order_info.getId());
                        break;
                    case "3"://确认完成
                        consent(order_info.getId());
                        break;
                    default:
                        textView4.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    /**
     * 完成任务
     * @param deli_id
     */
    public void verify(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_AFFIRM);
        map.put("user_id", "6");
        map.put("demand_id", "5");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 确认完成
     * @param deli_id
     */
    public void consent(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", TO_USER_AFFIRM);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", "5");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 立即抢单
     * @param deli_id
     */
    public void bid(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_DELIVERY);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("delivery_id", "2");
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 立即应邀
     * @param user_id
     */
    public void subscribe(String user_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", APPOINTMENT_SERVER);
        map.put("user_id",shared.getString("user_id", ""));
        map.put("server_id", "4");//order_info.getId()
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 立即应邀方法调用
     * @param position
     */
    @Override
    public void onInvited(int position) {
        subscribe("");
    }
}
