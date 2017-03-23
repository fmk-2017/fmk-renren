package com.example.everyoneassist.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.everyoneassist.R;

/**
 * Created by fengm on 2017-3-9.
 */

public class DialogShowUtils extends Dialog {

    private static DialogShowUtils dsu;
    private Context context;

    public DialogShowUtils(Context context) {
        super(context);
        this.context = context;
    }

    public DialogShowUtils(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogShowUtils(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static DialogShowUtils getInstance(Context context) {
        dsu = new DialogShowUtils(context);
        return dsu;
    }

    /**
     * 显示选择相册图片还是拍照图片
     *
     * @return 当前dialog的实例
     */
    public DialogShowUtils ShowGetImageType(View.OnClickListener oc) {
        View view = LayoutInflater.from(context).inflate(R.layout.getimagetype_dialog, null, false);
        TextView camera = (TextView) view.findViewById(R.id.camera);
        TextView album = (TextView) view.findViewById(R.id.album);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        camera.setOnClickListener(oc);
        album.setOnClickListener(oc);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsu.dismiss();
            }
        });
        dsu.setContentView(view);
        dsu.getWindow().getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
        dsu.getWindow().getAttributes().width = ScreenUtils.getScreenWidth(context);
        dsu.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        dsu.getWindow().setBackgroundDrawable(null);
        dsu.getWindow().setWindowAnimations(R.style.getimagetypeStily);
        dsu.create();
        dsu.show();
        return dsu;
    }


}
