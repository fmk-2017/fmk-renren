package com.example.dushikuaibang.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ToastUtils {

    public static void tipShort(Context context, String tipStr) {
        Toast.makeText(context, tipStr, Toast.LENGTH_LONG).show();
    }

}
