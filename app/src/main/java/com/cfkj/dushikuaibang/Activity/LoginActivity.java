package com.cfkj.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.AppUtils;
import com.cfkj.dushikuaibang.Utils.HttpPostRequestUtils;
import com.cfkj.dushikuaibang.Utils.ShareUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private final String METHOD_LOGIN = "login";  //添加技能
    private Button login;
    private TextView registered_go;
    private EditText phone, password;
    private String phones, passwords;
    private TextView tvWeChat, tvQQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        String username = shared.getString("username", "username");
        String passwords = shared.getString("passwords", "passwords");
        if (!username.equals("passwords") && !passwords.equals("passwords")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initView() {
        tvWeChat = (TextView) this.findViewById(R.id.tvWeChat);
        login = (Button) this.findViewById(R.id.login);
        registered_go = (TextView) this.findViewById(R.id.registered_go);
        phone = (EditText) this.findViewById(R.id.phone);
        password = (EditText) this.findViewById(R.id.password);
        tvQQ = (TextView) this.findViewById(R.id.tvQQ);

        registered_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkParam()) Login();
            }
        });
        tvQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(LoginActivity.this).sharePlat(QQ.NAME);
            }
        });
        tvWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(LoginActivity.this).sharePlat(Wechat.NAME);
            }
        });
    }

    private void Login() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_LOGIN);
        map.put("username", phones);
        map.put("password", passwords);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private boolean checkParam() {
        phones = phone.getText().toString().trim();
        passwords = password.getText().toString().trim();
        if (TextUtils.isEmpty(phones)) {
            Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.AuthorPhone(phones)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwords.length() < 6) {
            Toast.makeText(getApplicationContext(), "密码至少6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        shared.edit()
                .putString("username", phones)
                .putString("user_id", json.getJSONObject("data").getString("user_id"))
                .putString("user_photo", json.getJSONObject("data").getString("user_photo"))
                .putString("sex", json.getJSONObject("data").getString("sex"))
                .putString("email", json.getJSONObject("data").getString("email"))
                .putString("passwords", passwords)
                .commit();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
