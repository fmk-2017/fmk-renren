package com.cfkj.dushikuaibang.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cfkj.dushikuaibang.Activity.MainActivity;
import com.cfkj.dushikuaibang.Activity.ShoppingActivity;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.DialogShowUtils;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;
import com.cfkj.dushikuaibang.Utils.StrUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 * 同城
 */
public class CityFragment extends Fragment implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {

    private final String ACTION = "delivery_add";
    private DialogShowUtils dsu;
    private EditText tvSendPerson, tvSendPhone, tvSendAddress;
    private TextView tvCountMoney, tvPay;
    private EditText etRecePerson, etRecePhone, etTip, etRemarks, price, etReceAddress, etinfo;
    private String user_id, username;
    private SharedPreferences shared;
    private SeekBar city_seekbar;
    private int tip_price = 0;
    private Double Tprice = 0d;
    private String pay_type = "weixin";
    private IWXAPI api;

    public static CityFragment newInstance(String user_id, String username) {
        CityFragment fragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSendPerson = (EditText) view.findViewById(R.id.tvSendPerson);
        tvSendPhone = (EditText) view.findViewById(R.id.tvSendPhone);
        etRecePerson = (EditText) view.findViewById(R.id.etRecePerson);
        etRecePhone = (EditText) view.findViewById(R.id.etRecePhone);
        etTip = (EditText) view.findViewById(R.id.etTip);
        etRemarks = (EditText) view.findViewById(R.id.etRemarks);
        tvCountMoney = (TextView) view.findViewById(R.id.tvCountMoney);
        tvPay = (TextView) view.findViewById(R.id.tvPay);
        city_seekbar = (SeekBar) view.findViewById(R.id.city_seekbar);
        price = (EditText) view.findViewById(R.id.price);
        tvSendAddress = (EditText) view.findViewById(R.id.tvSendAddress);
        etReceAddress = (EditText) view.findViewById(R.id.etReceAddress);
        etinfo = (EditText) view.findViewById(R.id.etinfo);

        city_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tip_price = progress * 5;
                etTip.setText(tip_price + "元");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Tprice = Double.valueOf(s.toString()) + tip_price;
                tvCountMoney.setText(String.format("合计：%s元", Tprice + ""));
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user_id = getArguments().getString("user_id");
        username = getArguments().getString("username");

        tvPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPay://前往支付
                api = WXAPIFactory.createWXAPI(getActivity(), "wx463580e9dd2620d1");
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.getpaytype_dialog, null, false);
                dsu = DialogShowUtils.getInstance(getContext()).SelectPaytype(view);
                TextView alipay = (TextView) view.findViewById(R.id.alipay);
                TextView weixin = (TextView) view.findViewById(R.id.wxpay);
                TextView cancel = (TextView) view.findViewById(R.id.cancel);

                alipay.setOnClickListener(this);
                weixin.setOnClickListener(this);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dsu.dismiss();
                    }
                });
                break;
            case R.id.alipay:
                pay_type = "zhifubao";
                requestPay();
                dsu.dismiss();
                break;
            case R.id.wxpay:
                pay_type = "weixin";
                requestPay();
                dsu.dismiss();
                break;
        }
    }

    public void requestPay() {
        String mName = tvSendPerson.getText().toString().trim();
        String mPhone = tvSendPhone.getText().toString().trim();
        String maddress = tvSendAddress.getText().toString().trim();
        String recePerName = etRecePerson.getText().toString().trim();
        String recePerPhone = etRecePhone.getText().toString().trim();
        String recePeraddress = etReceAddress.getText().toString().trim();
        String prices = price.getText().toString().trim();
        String remarks = etRemarks.getText().toString().trim();
        String info = etinfo.getText().toString().trim();
        if (TextUtils.isEmpty(recePerName)) {
            etRecePerson.setError("不能为空");
            //ToastUtils.tipShort(getActivity(),"请填写收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(recePerPhone)) {
            etRecePhone.setError("不能为空");
            //ToastUtils.tipShort(getActivity(),"请填写收货人电话");
            return;
        }
        if (TextUtils.isEmpty(prices)) {
            price.setError("填写错误");
            //ToastUtils.tipShort(getActivity(),"电话号码填写错误!");
            return;
        }
        if (!StrUtils.isMobileNO(recePerPhone)) {
            etRecePhone.setError("填写错误");
            //ToastUtils.tipShort(getActivity(),"电话号码填写错误!");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("act", ACTION);
        map.put("user_id", shared.getString("user_id", ""));
        map.put("delivery_price", prices);
        map.put("delivery_username", mName);
        map.put("tip_price", tip_price + "");
        map.put("goods_address", maddress + "");
        map.put("phone", mPhone + "");
        map.put("shipping_address", recePeraddress + "");
        map.put("consignee", recePerName + "");
        map.put("delivery_phone", recePerPhone + "");
        map.put("remark", remarks + "");
        map.put("info", info + "");
        map.put("user_lon", ShoppingActivity.getInstance().user_lon);
        map.put("user_lat", ShoppingActivity.getInstance().user_lat);
        map.put("pay_type", pay_type);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (json.getString("result").equals("success")) {
            Toast.makeText(getActivity().getApplicationContext(), json.getString("info"), Toast.LENGTH_SHORT).show();
            if (pay_type.equals("zhifubao")) payali(json.getString("data"));
            else paywx(json.getJSONObject("data"));
        } else
            Toast.makeText(getActivity().getApplicationContext(), json.getString("info"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Fail(String method, String error) {
        Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void payali(final String orderInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.e("支付宝支付返回", result.toString());
                if ("9000".equals(result.get("resultStatus"))) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            getActivity().finish();
                        }
                    });
                }
            }
        }).start();
    }

    private void paywx(JSONObject json) throws JSONException {
        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = json.getString("package");
        req.sign = json.getString("sign");
//        req.extData = "app data"; // optional
        Toast.makeText(getActivity().getApplicationContext(), "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
        getActivity().finish();
    }

}
