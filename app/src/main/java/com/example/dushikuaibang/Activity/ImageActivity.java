package com.example.dushikuaibang.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.dushikuaibang.R;
import com.example.dushikuaibang.Utils.AppUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageActivity extends BaseActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initHeader("图片");

        url = getIntent().getStringExtra("url");

        ImageView imageview = (ImageView) this.findViewById(R.id.imageview);

        ImageLoader.getInstance().displayImage(url, imageview, AppUtils.getOptions());
    }
}
