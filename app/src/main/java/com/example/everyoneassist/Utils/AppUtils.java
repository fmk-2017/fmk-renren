package com.example.everyoneassist.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengm on 2017-3-4.
 */

public class AppUtils {


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


}
