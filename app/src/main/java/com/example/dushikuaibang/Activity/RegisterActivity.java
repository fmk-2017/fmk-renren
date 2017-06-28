package com.example.dushikuaibang.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dushikuaibang.Layout.PercentRelativeLayout;
import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;
import com.example.dushikuaibang.Utils.Constant;
import com.example.dushikuaibang.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.tencent.qq.QQ;

public class RegisterActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private final String METHOD_GET_VERIFIY = "get_verifiy";    //获取验证码
    private final String METHOD_REGISTER = "register";          //注册
    private TextView login_go, get_auth_code;
    private EditText phone, code, password;
    private Button register;
    private String phones, passwords, codes;// 手机号码，密码，验证码
    private Handler mhandler = new Handler();
    private int i;
    private Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            i--;
            if (i > 0) {
                get_auth_code.setText(i + "s");
                mhandler.postDelayed(mrunnable, 1000);
            } else {
                get_auth_code.setEnabled(true);
                get_auth_code.setText("获取验证码");
            }
        }
    };

    private boolean is_three = false;

    private String nickname;
    private String iconUri;
    private String openid;
    private String platName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        is_three = getIntent().getBooleanExtra("three", false);
        if (is_three) {
            initHeader("绑定手机");
            nickname = getIntent().getStringExtra("nick");
            iconUri = getIntent().getStringExtra("iconUri");
            openid = getIntent().getStringExtra("openid");
            platName = getIntent().getStringExtra("type");
        } else initHeader("注册");

        initView();

    }

    private void initView() {
        login_go = (TextView) this.findViewById(R.id.login_go);
        get_auth_code = (TextView) this.findViewById(R.id.get_auth_code);

        phone = (EditText) this.findViewById(R.id.phone);
        code = (EditText) this.findViewById(R.id.code);
        password = (EditText) this.findViewById(R.id.password);

        register = (Button) this.findViewById(R.id.register);
        PercentRelativeLayout bottom = (PercentRelativeLayout) this.findViewById(R.id.bottom);
        if (is_three) {
            bottom.setVisibility(View.GONE);
            register.setText("绑定");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkparam())
                    if (is_three) Bind();
                    else Regist();
            }
        });
        login_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        get_auth_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPhone()) return;
                GetAcode();
            }
        });

    }

    private boolean checkPhone() {
        phones = phone.getText().toString().trim();
        if (TextUtils.isEmpty(phones)) {
            Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!AppUtils.AuthorPhone(phones)) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkparam() {
        if (!checkPhone()) return false;
        codes = code.getText().toString().trim();
        passwords = password.getText().toString().trim();
        if (TextUtils.isEmpty(codes)) {
            Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
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

    public void GetAcode() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_GET_VERIFIY);
        map.put("mobile", phones);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void Regist() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", METHOD_REGISTER);
        map.put("username", phones);
        map.put("password", passwords);
        map.put("verify_code", codes);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    private void Bind() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("act", "register_openid");
        map.put("openid", openid);
        map.put("type", platName.equals(QQ.NAME) ? "qq" : "wechat");
        map.put("nickname", nickname);
        map.put("user_photo", iconUri);
        map.put("username", phones);
        map.put("password", passwords);
        map.put("verify_code", codes);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (METHOD_GET_VERIFIY.equals(method)) {
            i = 60;
            get_auth_code.setText(i + "s");
            get_auth_code.setEnabled(false);
            mhandler.postDelayed(mrunnable, 1000);
        } else if (METHOD_REGISTER.equals(method)) {
            finish();
        } else if ("register_openid".equals(method)) {
            Log.e("sssss", json.toString());
            getSharedPreferences(Constant.SHARED_NAME, Context.MODE_PRIVATE).edit()
                    .putString("username", json.getJSONObject("data").getString("username"))
                    .putString("user_id", json.getJSONObject("data").getString("user_id"))
                    .putString("user_photo", json.getJSONObject("data").getString("user_photo"))
                    .putString("sex", json.getJSONObject("data").getString("sex"))
                    .putString("email", json.getJSONObject("data").getString("email"))
                    .commit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void Fail(String method, String error) {
        Toast.makeText(getApplicationContext(), method + " 请求错误 " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
