package com.example.everyoneassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.everyoneassist.R;

public class AuthPhoneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone);
        initHeader("手机认证");


    }
}
