package com.example.dushikuaibang.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by fengm on 2017-3-20.
 */

public class MyScrollListView extends ListView {
    public MyScrollListView(Context context) {
        super(context);
    }

    public MyScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyScrollListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getFirstHeight() {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter == null) return 0;
        int totalHeight = 0;
        View listItem = listAdapter.getView(0, null, this);
        listItem.measure(0, 0); // 计算子项View 的宽高
        totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        int height = totalHeight + (this.getDividerHeight() * (listAdapter.getCount() - 1)) + 10;
        return height;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float rawY;
        float Y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rawY = ev.getRawY();
                Y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }
}
