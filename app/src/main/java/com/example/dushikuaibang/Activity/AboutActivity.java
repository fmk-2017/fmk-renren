package com.example.dushikuaibang.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;

public class AboutActivity extends BaseActivity {

    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initHeader("关于我们");

        version = (TextView) this.findViewById(R.id.version);

        String versions = AppUtils.getVersion(this);
        version.setText(versions);
    }
}
