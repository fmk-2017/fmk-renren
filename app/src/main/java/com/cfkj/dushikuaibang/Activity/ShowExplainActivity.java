package com.cfkj.dushikuaibang.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cfkj.dushikuaibang.R;

public class ShowExplainActivity extends BaseActivity {

    private TextView explain_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_explain);

        String header = getHeader();

        initHeader(header);

        explain_content = (TextView) this.findViewById(R.id.explain_content);
        explain_content.setText("今天去上街，看见一只猫，上面的痘痘数也数不清，大的像黄豆，小的像芝麻");

    }

    public String getHeader() {
        String header = "";
        int type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case 0:
                header = "人人帮使用协议";
                break;
            case 1:
                header = "服务者使用协议";
                break;
            case 2:
                header = "需求者使用协议";
                break;
            case 3:
                header = "技能管理交易流程";
                break;
            case 4:
                header = "需求管理交易流程";
                break;
            case 5:
                header = "审核规范";
                break;
            case 6:
                header = "服务费抽取说明";
                break;
            case 7:
                header = "常见问题";
                break;
        }
        return header;
    }
}
