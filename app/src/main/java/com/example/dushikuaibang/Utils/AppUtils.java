package com.example.dushikuaibang.Utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengm on 2017-3-4.
 */

public class AppUtils {


    private static DisplayImageOptions options;

    /**
     * 正则验证手机号码是否合法
     *
     * @param phones 手机号码
     * @return 是否为合法手机号码
     */
    public static boolean AuthorPhone(String phones) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(phones);
        return m.matches();
    }

    public static String getdistance(float distance) {
        String distances = "";
        if (distance > 1000) distances = (int) distance / 1000 + "KM";
        else distances = (int) distance + "M";
        return distances;
    }

    public static String getAvatarPath(String path) {
        if (path.contains("http:")) return path;
        return "http://112.74.35.236/" + path;
    }

    public static DisplayImageOptions getOptions() {
        if (options == null)
            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.ease_default_avatar)            //加载图片时的图片
//                    .showImageForEmptyUri(R.drawable.ease_default_avatar)         //没有图片资源时的默认图片
//                    .showImageOnFail(R.drawable.ease_default_avatar)              //加载失败时的图片
                    .cacheInMemory(true)                               //启用内存缓存
                    .cacheOnDisk(true)                                 //启用外存缓存
                    .considerExifParams(true)                          //启用EXIF和JPEG图像格式
                    //                .displayer(new RoundedBitmapDisplayer(20))         //设置显示风格这里是圆角矩形
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
        return options;
    }


}
