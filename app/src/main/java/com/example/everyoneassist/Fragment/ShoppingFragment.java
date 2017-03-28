package com.example.everyoneassist.Fragment;

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

import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.Utils.TimeUtils;
import com.example.everyoneassist.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/22.
 */

public class ShoppingFragment extends Fragment implements View.OnClickListener,HttpPostRequestUtils.HttpPostRequestCallback{
    private final String SEND_BUG_ACTION = "free_to_buy";

    private TextView tvSendDate,tvSure,tvDeliTip,tvContactPer,tvName;
    private EditText etGoodName,etBuyAddr,etReceAddr,edRemarks;

    private String user_id,username;

    public static ShoppingFragment newInstance(String user_id,String username){
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        bundle.putString("username",username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_fragment,container,false);
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
        tvDeliTip = (TextView) view.findViewById(R.id.tvDelTip);
        tvContactPer = (TextView) view.findViewById(R.id.tvContactPer);
        tvName = (TextView) view.findViewById(R.id.tvName);
        edRemarks = (EditText) view.findViewById(R.id.edRemarks);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvSendDate.setOnClickListener(this);
        tvSure.setOnClickListener(this);

        user_id = getArguments().getString("user_id","user_id");
        username = getArguments().getString("username","username");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvSendDate://选择发送日期
                ShowDate();
                break;
            case R.id.tvSure://确定
                inspectSure();
                break;
        }
    }


    private void inspectSure() {
        String goodName = etGoodName.getEditableText().toString().trim();
        String bugAddress = etBuyAddr.getEditableText().toString().trim();
        String receiveAddr = etReceAddr.getEditableText().toString().trim();
        String remarks = edRemarks.getEditableText().toString().trim();
        String date = tvSendDate.getText().toString();
        if(TextUtils.isEmpty(goodName)){
            etGoodName.setError("不能为空");
            ToastUtils.tipShort(getActivity(),"请填写购物名称");
        }
        if(TextUtils.isEmpty(bugAddress)){
            etBuyAddr.setError("不能为空");
            ToastUtils.tipShort(getActivity(),"请填写购买地址");
        }
        if(TextUtils.isEmpty(receiveAddr)){
            etBuyAddr.setError("不能为空");
            ToastUtils.tipShort(getActivity(),"请输入收货地址");
        }
        if(TextUtils.isEmpty(receiveAddr)){
            etReceAddr.setError("不能为空");
            ToastUtils.tipShort(getActivity(),"请输入收货地址");
        }
        if(date.equals("请选择取送达时间")){
            ToastUtils.tipShort(getActivity(),"请选择取送达时间");
        }

        if(!TextUtils.isEmpty(user_id) && !user_id.equals("user_id")){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("act", SEND_BUG_ACTION);
            map.put("user_id", user_id);
            map.put("goods_name", goodName);
            map.put("ship_time", TimeUtils.getTime(date));
            map.put("goods_address", bugAddress);
            map.put("shipping_address", receiveAddr);
            map.put("goods_price", "20");//tvDeliTip.getText().toString()
            map.put("username", username);
            map.put("phone", tvContactPer.getText().toString());
            map.put("desc", remarks);
            HttpPostRequestUtils.getInstance(this).Post(map);
        }else {
            ToastUtils.tipShort(getActivity(),"参数错误");
        }
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
                                (month+1) + "-" + dayOfMonth);
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
        Log.i("tag",json.toString());
    }

    @Override
    public void Fail(String method, String error) {
        Log.i("tag",error);
    }
}
