package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.example.dushikuaibang.Adapter.InvitationAdapter;
import com.example.dushikuaibang.Entity.Invitation;
import com.example.dushikuaibang.Entity.Order_Info;

import com.example.dushikuaibang.Layout.PercentLinearLayout;

import com.example.dushikuaibang.MyApplication;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.DialogShowUtils;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;
import com.example.dushikuaibang.Utils.TimeUtils;
import com.example.dushikuaibang.Utils.ToastUtils;
import com.example.dushikuaibang.View.CircleImageView;
import com.example.dushikuaibang.View.MyListView2;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtWillBuyActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback, View.OnClickListener {

    private final String DEMAND_INFO = "demand_info";
    private final String APPOINTMENT_SERVER = "user_order_save";
    private final String COMMENT = "sever_evaluate";
    private final String USER_ORDER_ADD = "user_order_add";
    private final String TO_USER_AFFIRM = "to_user_affirm";
    private final String USER_AFFIRM = "user_affirm";
    private final String Alipay = "zhifubao";
    private final String Weixin = "weixin";
    private String sk_names, avatar;
    private String demand_id, category_id;
    private MyListView2 mylistview;
    private Order_Info order_info;
    private List<Invitation> invitations;
    private TextView content, time, price, order_no, contacts, contacts_phone, remark, textView4, release_comment;
    private EditText comment;
    private ScrollView mySc;
    private InvitationAdapter ia;
    private boolean is_self;
    private PercentLinearLayout bottom;
    private LinearLayout no_city, city, contact, buyaddress_linear;
    private PercentLinearLayout topview;
    private TextView address_buy,/*随意购的购买地址*/
            address_receving; //其他的和随意购的收货地址
    private TextView shipper, shipper_phone, shipper_address;
    private TextView consignee, consignee_phone, consignee_address, sk_name;
    private TextView yyz;
    private IWXAPI api;
    private DialogShowUtils dsu;
    private String pay_type;
    private CircleImageView sk_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_will_buy);
        sk_names = getIntent().getStringExtra("title");
//        initHeader(sk_names);
        initHeader("需求详情");

        demand_id = getIntent().getStringExtra("xid");
        category_id = getIntent().getStringExtra("category_id");

        avatar = getIntent().getStringExtra("avatar");

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfo();
    }

    private void getInfo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", DEMAND_INFO);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", demand_id);
        map.put("category_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void initView() {
        mylistview = (MyListView2) this.findViewById(R.id.mylistview2);

        mylistview.setFocusable(false);
//        payali("app_id=2017010304811553&biz_content=%7B%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.01%22%2C%22subject%22%3A%22%E4%B8%87%E4%BA%8B%E9%80%9A%22%2C%22body%22%3A%22%E5%95%86%E5%93%81%E6%8F%8F%E8%BF%B0%22%2C%22out_trade_no%22%3A%22483054048429922789%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F112.74.35.236%2Fapi.php&timestamp=2017-05-04+13%3A47%3A20&version=1.0&sign_type=RSA&sign=RGzYPzNZI%2BRxS7yq5rRrC%2BRK2J2RSNTdAWe5TSpW4iGH0l8n0UBlenNrsKotgYq4XC%2FPjyDPI5Xb0ssyH8LTp%2FYVKKI9Zzr2fPKyGbieojvyvirm7rlA%2Fv%2FPkN4Tl5QyyJOC%2FRpbM7kPPMqHVUY9BhEOrLO7NTgn7Phu%2Fhjh3Ho%3D");

        sk_avatar = (CircleImageView) this.findViewById(R.id.sk_avatar);
        sk_name = (TextView) this.findViewById(R.id.sk_name);
        sk_name.setText(sk_names);
        if (!TextUtils.isEmpty(avatar))
            ImageLoader.getInstance().displayImage(avatar, sk_avatar);
//        x.image().bind(sk_avatar, avatar, MyApplication.io);

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
        bottom = (PercentLinearLayout) this.findViewById(R.id.bottom);

        topview = (PercentLinearLayout) this.findViewById(R.id.topview);
        no_city = (LinearLayout) this.findViewById(R.id.no_city);
        city = (LinearLayout) this.findViewById(R.id.city);
        contact = (LinearLayout) this.findViewById(R.id.contact);
        buyaddress_linear = (LinearLayout) this.findViewById(R.id.buyaddress_linear);

        yyz = (TextView) this.findViewById(R.id.yyz);

        shipper = (TextView) this.findViewById(R.id.shipper);
        shipper_phone = (TextView) this.findViewById(R.id.shipper_phone);
        shipper_address = (TextView) this.findViewById(R.id.shipper_address);

        consignee = (TextView) this.findViewById(R.id.consignee);
        consignee_phone = (TextView) this.findViewById(R.id.consignee_phone);
        consignee_address = (TextView) this.findViewById(R.id.consignee_address);

        comment = (EditText) this.findViewById(R.id.comment);

        textView4.setOnClickListener(this);
        release_comment.setOnClickListener(this);

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
        content.setText(order_info.getInfo());
        if ("10".equals(order_info.getCategory_id())) {
            no_city.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            topview.setVisibility(View.GONE);
            city.setVisibility(View.VISIBLE);
            shipper.setText(String.format("发货人：%s", order_info.getDelivery_userName()));
            shipper_phone.setText(String.format("联系电话：%s", order_info.getDelivery_phone()));
            shipper_address.setText(String.format("发货地址：%s", order_info.getGoods_address()));

            consignee.setText(String.format("收货人：%s", order_info.getConsignee()));
            consignee_phone.setText(String.format("联系电话：%s", order_info.getPhone()));
            consignee_address.setText(String.format("收货地址：%s", order_info.getShipping_address()));
        } else if ("2".equals(order_info.getCategory_id())) {
            city.setVisibility(View.GONE);
            topview.setVisibility(View.GONE);
            no_city.setVisibility(View.VISIBLE);
            buyaddress_linear.setVisibility(View.VISIBLE);
            contacts.setText(String.format("收货人：%s", order_info.getUsername() + " "));
            contacts_phone.setText(String.format("联系电话：%s", order_info.getPhone()));
            address_receving.setText(String.format("收货地址：%s", order_info.getShipping_address()));
            address_buy.setText(order_info.getGoods_address());
            content.setText(order_info.getGoods_name());
        } else {
            topview.setVisibility(View.VISIBLE);
            no_city.setVisibility(View.GONE);
            city.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            buyaddress_linear.setVisibility(View.GONE);
            address_receving.setVisibility(View.GONE);
            contacts.setText(String.format("联系人：%s", order_info.getConsignee()));
            contacts_phone.setText(String.format("联系电话：%s", order_info.getTel()));
        }

        price.setText("服务费：" + (TextUtils.isEmpty(order_info.getServer_price()) ? "详聊" : order_info.getServer_price()));

        time.setText(String.format("发布时间：%s", TimeUtils.getFormatTime(order_info.getAddtime())));
        order_no.setText("订单号：" + order_info.getD_order());
        remark.setText("备注：" + order_info.getRemark());
        textView4.setEnabled(true);

        String op = "";
        switch (TextUtils.isEmpty(order_info.getStatus()) ? "3" : order_info.getStatus()) {
            case "0":
                api = WXAPIFactory.createWXAPI(this, "wx463580e9dd2620d1");
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
//                    mylistview.setVisibility(View.GONE);
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
            case "10":
                if (shared.getString("user_id", "").equals(order_info.getUser_id())) {
                    op = "等待接单";
                    textView4.setEnabled(false);
                } else op = "确认接单";
                break;
            default:
                textView4.setVisibility(View.GONE);
                break;
        }
        textView4.setText(op);
    }

    private String dataStr(String str) {
        return TextUtils.isEmpty(str) ? "暂无数据" : str;
    }

    private void initListener() {
        textView4.setOnClickListener(this);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (DEMAND_INFO.equals(method)) {
            order_info = JSON.parseObject(json.getJSONObject("data").getString("order_info"), Order_Info.class);
            if (json.getJSONObject("data").has("user_info")) {
                invitations = JSON.parseArray(json.getJSONObject("data").getString("user_info"), Invitation.class);
                for (Invitation invitation : invitations) {
                    if (shared.getString("user_id", "").equals(invitation.getUser_id()))
                        textView4.setEnabled(false);
                }
            }
            setviewData();
            if (invitations != null && invitations.size() > 0) {
                if (order_info.getUser_id().equals(shared.getString("user_id", ""))) is_self = true;
                else is_self = false;
                ia = new InvitationAdapter(this, invitations, this, is_self);
                mylistview.setAdapter(ia);
                yyz.setVisibility(View.GONE);
            } else {
                yyz.setVisibility(View.VISIBLE);
                mylistview.setVisibility(View.GONE);
            }
        } else if (APPOINTMENT_SERVER.equals(method)) {
            Toast.makeText(getApplicationContext(), "已选择应邀者", Toast.LENGTH_SHORT).show();
            if (Alipay.equals(pay_type)) payali(json.getString("data"));
            else paywx(json.getJSONObject("data"));
        } else if (USER_AFFIRM.equals(method)) {
            Toast.makeText(getApplicationContext(), "确认服务完成", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (TO_USER_AFFIRM.equals(method)) {
            Toast.makeText(getApplicationContext(), "服务完成", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (COMMENT.equals(method)) {
            Toast.makeText(getApplicationContext(), "已评价", Toast.LENGTH_SHORT).show();
            getInfo();
        } else if (USER_ORDER_ADD.equals(method)) {
            Toast.makeText(getApplicationContext(), "抢单成功,请关注【我的订单】", Toast.LENGTH_SHORT).show();
//            getInfo();
            finish();
        } else if ("orders".equals(method)) {
            Toast.makeText(getApplicationContext(), "已成功接单", Toast.LENGTH_SHORT).show();
            getInfo();
        }
    }

    private void payali(final String orderInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AtWillBuyActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.e("支付宝支付返回", result.toString());
                if ("9000".equals(result.get("resultStatus"))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getInfo();
                        }
                    });
                }
            }
        }).start();
    }

    private void paywx(JSONObject json) throws JSONException {
        PayReq req = new PayReq();
        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = json.getString("package");
        req.sign = json.getString("sign");
        req.extData = "app data"; // optional
        Toast.makeText(getApplicationContext(), "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    @Override
    public void Fail(String method, String error) {
        ToastUtils.tipShort(AtWillBuyActivity.this, error);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitantion:
                final int position = (int) v.getTag();//暂时不用
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.getpaytype_dialog, null, false);
                dsu = DialogShowUtils.getInstance(this).SelectPaytype(view);
                TextView alipay = (TextView) view.findViewById(R.id.alipay);
                TextView weixin = (TextView) view.findViewById(R.id.wxpay);
                TextView cancel = (TextView) view.findViewById(R.id.cancel);
                alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_type = Alipay;
                        subscribe(invitations.get(position).getUser_id(), invitations.get(position).getTender_id(), Alipay);
                        dsu.dismiss();
                    }
                });
                weixin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_type = Weixin;
                        subscribe(invitations.get(position).getUser_id(), invitations.get(position).getTender_id(), Weixin);
                        dsu.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dsu.dismiss();
                    }
                });
                break;
            case R.id.textView4:
                switch (order_info.getStatus()) {//order_info.getStatus()
                    case "0"://立即抢单
                        bid(order_info.getId());
                        break;
                    case "1"://完成任务
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setMessage("完成任务？");
                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                verify(order_info.getId());
                            }
                        });
                        alertDialog.setNegativeButton("取消", null);
                        alertDialog.create().show();
                        break;
                    case "2"://确认完成
                        consent(order_info.getId());
                        break;
                    case "3"://品论
//                        verify(order_info.getId());
                        break;
                    case "10":
                        receiving(order_info.getId());
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

    /**
     * 接单
     * @param order_id      需求订单id
     */
    public void receiving(String order_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", "orders");
        map.put("user_id", shared.getString("user_id", ""));
        map.put("id", order_id);
        map.put("cat_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 完成任务
     *
     * @param deli_id   需求订单id
     */
    public void verify(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", TO_USER_AFFIRM);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        map.put("category_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 确认完成
     *
     * @param deli_id   需求订单id
     */
    public void consent(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_AFFIRM);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        map.put("category_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 立即抢单
     *
     * @param deli_id   需求订单id
     */
    public void bid(String deli_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("act", USER_ORDER_ADD);
        map.put("to_user_id", shared.getString("user_id", ""));
        map.put("demand_id", deli_id);
        map.put("category_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 立即应邀
     *
     * @param user_id
     */
    public void subscribe(String user_id, String tender_id, String type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", APPOINTMENT_SERVER);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("demand_id", order_info.getId());
        map.put("to_user_id", user_id);
        map.put("tender_id", tender_id);
        map.put("type", type);
        map.put("category_id", category_id);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /**
     * 评论的
     *
     * @param comment
     */
    public void comment(String comment) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", COMMENT);
        map.put("category_id", category_id);
        map.put("score", "3");
        map.put("evaluate_content", comment);
        map.put("server_id", order_info.getId());
        map.put("evaluate_user_id", shared.getString("user_id", ""));
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

//    /**
//     * 立即应邀方法调用
//     *
//     * @param position
//     */
//    @Override
//    public void onInvited(int position) {
//        subscribe("");
//    }
}
