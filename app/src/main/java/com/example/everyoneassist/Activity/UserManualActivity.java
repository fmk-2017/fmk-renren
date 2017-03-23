package com.example.everyoneassist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.everyoneassist.R;

public class UserManualActivity extends BaseActivity implements View.OnClickListener {

    private TextView[] textviews = new TextView[8];//renren_protocol,server_protocol,need_protocol,skill_manager_protocol,need_manager_protocol,auditing_criterion,service_charge_explain,faq

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);
        initHeader("使用手册");
//        setRightImg(R.drawable.ic_chevron_right_black_20dp);
        initView();
    }

    private void initView() {
        textviews[0] = (TextView) this.findViewById(R.id.renren_protocol);
        textviews[1] = (TextView) this.findViewById(R.id.server_protocol);
        textviews[2] = (TextView) this.findViewById(R.id.need_protocol);
        textviews[3] = (TextView) this.findViewById(R.id.skill_manager_protocol);
        textviews[4] = (TextView) this.findViewById(R.id.need_manager_protocol);
        textviews[5] = (TextView) this.findViewById(R.id.auditing_criterion);
        textviews[6] = (TextView) this.findViewById(R.id.service_charge_explain);
        textviews[7] = (TextView) this.findViewById(R.id.faq);
        for (TextView textview : textviews) {
            textview.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ShowExplainActivity.class);
        switch (v.getId()) {
            case R.id.renren_protocol:
                intent.putExtra("type", 0);
                break;
            case R.id.server_protocol:
                intent.putExtra("type", 1);
                break;
            case R.id.need_protocol:
                intent.putExtra("type", 2);
                break;
            case R.id.skill_manager_protocol:
                intent.putExtra("type", 3);
                break;
            case R.id.need_manager_protocol:
                intent.putExtra("type", 4);
                break;
            case R.id.auditing_criterion:
                intent.putExtra("type", 5);
                break;
            case R.id.service_charge_explain:
                intent.putExtra("type", 6);
                break;
            case R.id.faq:
                intent.putExtra("type", 7);
                break;
        }
        startActivity(intent);
    }
}
