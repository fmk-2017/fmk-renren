package com.example.dushikuaibang.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.dushikuaibang.R;

/**
 * Created by Administrator on 2017/3/29.
 */

public class SelectDialog implements View.OnClickListener {
    private Dialog dialog;
    private Build build;
    private View view;
    private TextView tvPai, tvXiangCe, tvCan;

    public SelectDialog(Build build) {
        this.build = build;
        dialog = new Dialog(build.getContext(), R.style.bottomDialogStyle);
        view = LayoutInflater.from(build.getContext()).inflate(R.layout.dialog_picture_view, null);
        dialog.setContentView(view);
        tvPai = (TextView) view.findViewById(R.id.tvPai);
        tvXiangCe = (TextView) view.findViewById(R.id.tvXiangCe);
        tvCan = (TextView) view.findViewById(R.id.tvCan);

        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        dialog.show();

        tvPai.setText(build.getXiangJi());
        tvXiangCe.setText(build.getXiangCe());
        tvCan.setText(build.getQuxiao());

        tvPai.setOnClickListener(this);
        tvXiangCe.setOnClickListener(this);
        tvCan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPai://拍照
                build.getOnClickNum().getNum(1);
                break;
            case R.id.tvXiangCe://相册
                build.getOnClickNum().getNum(2);
                break;
            case R.id.tvCan://取消
                build.getOnClickNum().getNum(3);
                break;
        }
        dialog.dismiss();
    }

    public interface OnClickNum {
        void getNum(int num);
    }

    public static class Build {
        Context context;
        String xiangJi;
        String xiangCe;
        String quxiao;
        OnClickNum onClickNum;

        public Build(Context context) {
            this.context = context;
            xiangJi = "拍照";
            xiangCe = "相册";
            quxiao = "取消";
        }

        public SelectDialog build() {
            return new SelectDialog(this);
        }

        public String getXiangJi() {
            return xiangJi;
        }

        public Build setXiangJi(String xiangJi) {
            this.xiangJi = xiangJi;
            return this;
        }

        public String getXiangCe() {
            return xiangCe;
        }

        public Build setXiangCe(String xiangCe) {
            this.xiangCe = xiangCe;
            return this;
        }

        public Context getContext() {
            return context;
        }

        public OnClickNum getOnClickNum() {
            return onClickNum;
        }

        public Build setOnClickNum(OnClickNum onClickNum) {
            this.onClickNum = onClickNum;
            return this;
        }

        public String getQuxiao() {
            return quxiao;
        }

        public Build setQuxiao(String quxiao) {
            this.quxiao = quxiao;
            return this;
        }
    }

}
