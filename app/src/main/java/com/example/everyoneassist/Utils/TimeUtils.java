package com.example.everyoneassist.Utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
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

    //字符串转时间戳
    public static String getTime(String timeString){
        if (TextUtils.isEmpty(timeString)) return "";
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

}
