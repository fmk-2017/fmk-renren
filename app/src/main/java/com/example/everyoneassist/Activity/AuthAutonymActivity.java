package com.example.everyoneassist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.everyoneassist.R;

public class AuthAutonymActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_autonym);
        initHeader("实名认证");


    }
}
