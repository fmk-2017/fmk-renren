package com.example.everyoneassist;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by fengm on 2017-3-4.
 */

public class MyApplication extends Application {


    public static ImageOptions io;
    private static MyApplication context;

    public static MyApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        io = new ImageOptions.Builder().setCrop(true).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setFadeIn(true).setIgnoreGif(true).build();

        ImageLoaderConfiguration ilc = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(5)
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).build();
        ImageLoader.getInstance().init(ilc);

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);


    }
}
