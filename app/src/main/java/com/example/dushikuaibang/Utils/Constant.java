package com.example.dushikuaibang.Utils;

import com.example.dushikuaibang.MyApplication;

import java.io.File;

/**
 * Created by fengm on 2017-3-4.
 */

public class Constant {

    /**
     * POST请求地址
     */
    public static final String Url = "http://112.74.35.236/api.php";//"http://m.szwtdl.cn/api.php";//
    //    public static final String Url = "http://sunyan.xn--6qq986b3xl/api.php";//"http://m.szwtdl.cn/api.php";//
    public static final String HOST = "http://112.74.35.236/";
//    public static final String HOST = "http://sunyan.xn--6qq986b3xl/";

    public static final String SHARED_NAME = "user";

    public static final String appid = "wx3dd3865f1adf4c71";

    /**
     * 文件存储跟目录路径
     */
    public final static String APP_PATH = "app";
    public final static String IMAGE_PATH = "image";

    public static String getAppPath(String Dir) {
        File file = MyApplication.getInstance().getExternalFilesDir(Dir);
        if (!file.exists()) file.mkdirs();
        return file.getAbsolutePath();
    }


}
