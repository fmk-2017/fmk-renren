package com.example.everyoneassist.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.AppUtils;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private final String METHOD_LOGIN = "login";  //添加技能
    private Button login;
    private TextView registered_go;
    private EditText phone, password;
    private String phones, passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        login = (Button) this.findViewById(R.id.login);
        registered_go = (TextView) this.findViewById(R.id.registered_go);
        phone = (EditText) this.findViewById(R.id.phone);
        password = (EditText) this.findViewById(R.id.password);

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
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.AuthorPhone(phones)) {
            Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwords.length() < 6) {
            Toast.makeText(this, "密码至少6位", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        shared.edit().putString("username", phones).putString("user_id", json.getJSONObject("data").getString("user_id")).commit();
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
