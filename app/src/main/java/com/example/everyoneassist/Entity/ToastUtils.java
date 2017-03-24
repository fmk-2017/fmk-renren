package com.example.everyoneassist.Entity;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ToastUtils {

    public static void tipShort(Context context,String tipStr){
        Toast.makeText(context,"请填写收货人姓名",Toast.LENGTH_LONG).show();
    }

}
