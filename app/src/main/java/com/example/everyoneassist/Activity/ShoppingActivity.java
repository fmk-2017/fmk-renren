package com.example.everyoneassist.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.everyoneassist.Fragment.CityFragment;
import com.example.everyoneassist.Fragment.ShoppingFragment;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */

public class ShoppingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rgShopp;
    private RadioButton rbCity,rbShopp;
    private FrameLayout fl_contain;

    private FragmentManager mFrManager;
    private Fragment mFragment = null;
    private List<Fragment> fragments = new ArrayList<>();

    private SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        intiView();
        initData();
    }

    /**
     * 初始化数据，并默认显示同城快递
     */
    private void initData() {
        shared = getSharedPreferences(Constant.SHARED_NAME, MODE_PRIVATE);
        String user_id = shared.getString("user_id","user_id");
        String username = shared.getString("username","username");
        fragments.add(new CityFragment().newInstance(user_id,username));
        fragments.add(new ShoppingFragment().newInstance(user_id,username));

        rgShopp.setOnCheckedChangeListener(this);
        mFrManager = getSupportFragmentManager();
        FragmentTransaction frTransaction = mFrManager.beginTransaction();
        mFragment = fragments.get(0);
        if(mFragment != null){
            frTransaction.add(R.id.fl_contain,mFragment);
            frTransaction.addToBackStack(null);
            frTransaction.commit();
        }
    }

    /**
     * 初始化控件
     */
    private void intiView() {
        rgShopp = (RadioGroup) findViewById(R.id.rgShopp);
        rbCity = (RadioButton) findViewById(R.id.rbCity);
        rbShopp = (RadioButton) findViewById(R.id.rbShopp);
        fl_contain = (FrameLayout) findViewById(R.id.fl_contain);
    }

    @Override
    public void onCheckedChanged(RadioGroup group,int checkedId) {
        FragmentTransaction _frTransaction = mFrManager.beginTransaction();
        _frTransaction.hide(mFragment);//隐藏之前的界面
        switch (checkedId){
            case R.id.rbCity://同城快递
                mFragment = fragments.get(0);
                break;
            case R.id.rbShopp://随意购
                mFragment = fragments.get(1);
                break;
        }
        if(mFragment != null && mFragment.isAdded()){//添加过的直接显示
            _frTransaction.show(mFragment);
        }else if(mFragment != null){
            _frTransaction.add(R.id.fl_contain,mFragment);
            _frTransaction.addToBackStack(null);
        }
        _frTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
           ShoppingActivity.this.finish();
        return super.onKeyDown(keyCode, event);
    }
}
