package com.example.everyoneassist.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fengm on 2017-3-11.
 */

public class TimeUtils {


    public static String getTime_difference(String timestamps) {
        String timediff = "";
        long timestamp = Long.valueOf(timestamps);
        long difference = System.currentTimeMillis() / 1000l - timestamp;
        if (difference < 60000) timediff = (int) (difference / 1000) + "秒";
        else if (difference < 3600000) timediff = (int) (difference / 60000) + "分";
        else if (difference < 86400000) timediff = (int) (difference / 3600000) + "小时";
        else timediff = (int) (difference / 86400000) + "天";
        return timediff;
    }

    public static String getFormatTime(String timestamps) {
        long timestamp = Long.valueOf(timestamps);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sf.format(new Date(timestamp * 1000l));
    }


}
