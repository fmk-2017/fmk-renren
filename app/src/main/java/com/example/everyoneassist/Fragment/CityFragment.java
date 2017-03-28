package com.example.everyoneassist.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.StrUtils;
import com.example.everyoneassist.Utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CityFragment extends Fragment implements View.OnClickListener{

    private TextView tvSendPerson,tvSendPhone,tvCountMoney,tvPay;
    private EditText etRecePerson,etRecePhone,etTip,etRemarks;

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

                break;
        }
    }
}
