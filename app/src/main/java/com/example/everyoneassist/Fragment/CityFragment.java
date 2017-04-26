package com.example.everyoneassist.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.StrUtils;
import com.example.everyoneassist.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CityFragment extends Fragment implements View.OnClickListener,HttpPostRequestUtils.HttpPostRequestCallback{

    private final String ACTION = "delivery_add";

    private TextView tvSendPerson,tvSendPhone,tvCountMoney,tvPay;
    private EditText etRecePerson,etRecePhone,etTip,etRemarks;
    private String user_id,username;

    public static CityFragment newInstance(String user_id,String username){
        CityFragment fragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        bundle.putString("username",username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_fragment,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSendPerson = (TextView) view.findViewById(R.id.tvSendPerson);
        tvSendPhone = (TextView) view.findViewById(R.id.tvSendPhone);
        etRecePerson = (EditText) view.findViewById(R.id.etRecePerson);
        etRecePhone = (EditText) view.findViewById(R.id.etRecePhone);
        etTip = (EditText) view.findViewById(R.id.etTip);
        etRemarks = (EditText) view.findViewById(R.id.etRemarks);
        tvCountMoney = (TextView) view.findViewById(R.id.tvCountMoney);
        tvPay = (TextView) view.findViewById(R.id.tvPay);

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
        switch (v.getId()){
            case R.id.tvPay://前往支付
                String recePerName = etRecePerson.getEditableText().toString().trim();
                String recePerPhone = etRecePhone.getEditableText().toString().trim();
                String remarks = etRemarks.getEditableText().toString().trim();
                if(TextUtils.isEmpty(recePerName)){
                    etRecePerson.setError("不能为空");
                    ToastUtils.tipShort(getActivity(),"请填写收货人姓名");
                    return;
                }
                if(TextUtils.isEmpty(recePerPhone)){
                    etRecePhone.setError("不能为空");
                    ToastUtils.tipShort(getActivity(),"请填写收货人电话");
                    return;
                }
                if(!StrUtils.isMobileNO(recePerPhone)){
                    etRecePhone.setError("填写错误");
                    ToastUtils.tipShort(getActivity(),"电话号码填写错误!");
                    return;
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("act", ACTION);
                map.put("user_id","");
                map.put("delivery_price","");
                map.put("tip_price","");
                map.put("goods_name","");
                map.put("goods_weight","");
                map.put("goods_volume","");
                map.put("goods_price","");
                map.put("goods_info","");
                map.put("start_address","");
                map.put("user_lon","");
                map.put("user_lat","");
                map.put("end_address","");
                map.put("take_addtime","");
                map.put("consignee","");
                map.put("receiving_phone","");
                HttpPostRequestUtils.getInstance(this).Post(map);
                break;
        }
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (json.getString("result").equals("success")) Toast.makeText(getActivity(),json.getString("info"),Toast.LENGTH_SHORT).show();
        else Toast.makeText(getActivity(),json.getString("info"),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Fail(String method, String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }
}
