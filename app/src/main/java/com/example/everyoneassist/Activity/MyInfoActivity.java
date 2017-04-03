package com.example.everyoneassist.Activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.everyoneassist.Adapter.MyCollectAdapter;
import com.example.everyoneassist.R;
import com.example.everyoneassist.View.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyInfoActivity extends BaseActivity {

    private CircleImageView ivMeHead;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_my_info);

        initview();

    }

    private void initview() {
        ivMeHead = (CircleImageView) findViewById(R.id.ivMeHead);
        tvUserName = (TextView) findViewById(R.id.tvUserName);

        String user_photo = shared.getString("user_photo", "user_photo");
        String username = shared.getString("username","username");

        if (!username.equals("user_photo")){
            ImageLoader.getInstance().displayImage(user_photo, ivMeHead);
        }
        if (username.equals("user_id")){
            tvUserName.setText("人人帮");
        }else{
            tvUserName.setText(username);
        }
    }

}
