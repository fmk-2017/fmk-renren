package com.cfkj.dushikuaibang.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cfkj.dushikuaibang.Activity.ShoppingActivity;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.DialogShowUtils;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;
import com.cfkj.dushikuaibang.Utils.TimeUtils;
import com.cfkj.dushikuaibang.Utils.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 * 随意购
 */

public class ShoppingFragment extends Fragment implements View.OnClickListener, HttpPostRequestUtils.HttpPostRequestCallback {
    private final String ACTION = "free_to_buy";

    private TextView tvSendDate, tvSure;
    private EditText etGoodName, etBuyAddr, etReceAddr, edRemarks, tvDeliTip, tvContactPer, tvName;
    private String user_id, username;
    private String pay_type = "weixin";
    private DialogShowUtils dsu;
    private IWXAPI api;

    public static ShoppingFragment newInstance(String user_id, String username) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id", user_id);
        bundle.putString("username", username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSendDate = (TextView) view.findViewById(R.id.tvSendDate);
        tvSure = (TextView) view.findViewById(R.id.tvSure);
        etGoodName = (EditText) view.findViewById(R.id.etGoodName);
        etBuyAddr = (EditText) view.findViewById(R.id.etBuyAddr);
        etReceAddr = (EditText) view.findViewById(R.id.etReceAddr);
        tvDeliTip = (EditText) view.findViewById(R.id.tvDeliTip);
        tvContactPer = (EditText) view.findViewById(R.id.tvContactPer);
        tvName = (EditText) view.findViewById(R.id.tvName);
        edRemarks = (EditText) view.findViewById(R.id.edRemarks);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user_id = getArguments().getString("user_id");
        username = getArguments().getString("username");

        tvSendDate.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendDate://选择发送日期
                ShowDate();
                break;
            case R.id.tvSure://确定
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
                            getActivity().finish();
                        }
                    });
                }
            }
        }).start();
    }

    public void requestPay() {

        HashMap<String, String> map = new HashMap<String, String>();
        String goodName = etGoodName.getEditableText().toString().trim();
        String bugAddress = etBuyAddr.getEditableText().toString().trim();
        String receiveAddr = etReceAddr.getEditableText().toString().trim();
        String remarks = edRemarks.getEditableText().toString().trim();
        String goods_price = tvDeliTip.getText().toString().trim();
        String username = tvName.getEditableText().toString().trim();
        String phone = tvContactPer.getEditableText().toString().trim();

        if (TextUtils.isEmpty(goods_price)) {
            tvDeliTip.setError("不能为空");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            tvName.setError("不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            tvContactPer.setError("不能为空");
            return;
        }
        if (TextUtils.isEmpty(goodName)) {
            etGoodName.setError("不能为空");
            ToastUtils.tipShort(getActivity(), "请填写购买商品名称");
            return;
        }
        if (TextUtils.isEmpty(bugAddress)) {
            etBuyAddr.setError("不能为空");
            ToastUtils.tipShort(getActivity(), "请输入购买地址");
            return;
        }
        if (TextUtils.isEmpty(receiveAddr)) {
            etReceAddr.setError("不能为空");
            ToastUtils.tipShort(getActivity(), "请输入收货地址");
            return;
        }
        String time = tvSendDate.getText().toString();
        if (time.equals("请选择送达时间")) {
            ToastUtils.tipShort(getActivity(), "请选择");
            return;
        }

        map.put("pay_type", pay_type);
        map.put("act", ACTION);
        map.put("user_id", user_id);
        map.put("goods_name", goodName);
        map.put("ship_time", TimeUtils.getTime(time));
        map.put("goods_address", bugAddress);
        map.put("shipping_address", receiveAddr);
        map.put("goods_price", goods_price);
        map.put("username", username);
        map.put("phone", phone);
        map.put("desc", remarks);
        map.put("user_lon", ShoppingActivity.getInstance().user_lon);
        map.put("user_lat", ShoppingActivity.getInstance().user_lat);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    /*
   日期选择器
    */
    private void ShowDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker,
                                          int year, int month, int dayOfMonth) {
                        //Calendar月份是从0开始,所以month要加1
                        tvSendDate.setText("" + year + "-" +
                                (month + 1) + "-" + dayOfMonth);
                    }
                };
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
//        getActivity().finish();
        if (json.getString("result").equals("success")) { //暂时去掉
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
