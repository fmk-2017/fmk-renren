package com.example.dushikuaibang.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by fengm on 2017-3-5.
 */

public class MyListView extends ListView {

    private Context context;
    private TextView footer;

    public MyListView(Context context) {
        super(context);
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        footer = new TextView(context);
        footer.setBackgroundColor(Color.WHITE);
        footer.setText("------这是底线------");
        footer.setGravity(Gravity.CENTER);
        addFooterView(footer);
    }

    /**
     * 底部的下边内边距
     *
     * @param bottom 下内边距  px
     */
    public void setbottom(int bottom) {
        footer.setPadding(10, 10, 10, bottom);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }
}
