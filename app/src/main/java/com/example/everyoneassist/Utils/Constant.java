package com.example.everyoneassist.Utils;

import com.example.everyoneassist.MyApplication;

import java.io.File;

/**
 * Created by fengm on 2017-3-4.
 */

public class Constant {


    /**
     * POST请求地址
     */
    public static final String Url = "http://m.szwtdl.cn/api.php";


    public static final String SHARED_NAME = "user";


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
