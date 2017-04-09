package com.example.everyoneassist.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.everyoneassist.Adapter.SkillAdapter;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;
import com.example.everyoneassist.View.Pull.PullToRefreshExpandableListView;
import com.example.everyoneassist.View.Pull.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleServerActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private PullToRefreshExpandableListView refreshlistview;
    private ExpandableListView expandableListView;
    private String cid, cname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_server);
        gettitle();
        initHeader(cname);

        initView();

    }

    private void initView() {
        refreshlistview = (PullToRefreshExpandableListView) this.findViewById(R.id.refreshlistview);
        expandableListView = refreshlistview.getRefreshableView();
        expandableListView.collapseGroup(0);
        expandableListView.expandGroup(0);

    }

    public void gettitle() {
        cid = getIntent().getStringExtra("cid");
        cname = getIntent().getStringExtra("cname");
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {

    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}
