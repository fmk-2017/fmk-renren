package com.example.dushikuaibang.Utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.dushikuaibang.Entity.GoodsNameAndId;
import com.example.dushikuaibang.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 窗口管理工具
 *
 * @author wjh
 */
public class WindowsUtils {

    /**
     * 全屏带背景的ListView PopupWindow展示框
     *
     * @param locationView
     * @param list
     * @param listener
     */
    public static void showStringListPopupWindow(View locationView, List<GoodsNameAndId> list, final OnStringItemClickListener listener) {
        Log.i("cyb", "创建showStringListPopupWindow");
        if (list == null || list.size() == 0) {
            return;
        }
        final int[] select = new int[]{0, 0};
        Context context = locationView.getContext();
        final PopupWindow window = new PopupWindow(context);
        window.setWidth(LayoutParams.MATCH_PARENT);
        window.setHeight(LayoutParams.MATCH_PARENT);
        View view = View.inflate(context, R.layout.popup_window_server_type, null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        GridView sexs = (GridView) view.findViewById(R.id.sexs);
        TextView reset = (TextView) view.findViewById(R.id.reset);
        TextView tvSure = (TextView) view.findViewById(R.id.tvSure);

        GridAdapter adapter = new GridAdapter(context, list);
        gridview.setAdapter(adapter);
        List<GoodsNameAndId> list2 = new ArrayList<>();
        list2.add(new GoodsNameAndId("不限", 0));
        list2.add(new GoodsNameAndId("男", 1));
        list2.add(new GoodsNameAndId("女", 2));
        GridAdapter adapter2 = new GridAdapter(context, list2);
        sexs.setAdapter(adapter2);

        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select[0] = position;
                if (view.isSelected()) view.setSelected(false);
                else view.setSelected(true);
            }
        });
        sexs.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.isSelected()) view.setSelected(false);
                else view.setSelected(true);
                select[1] = position;
            }
        });
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                select[0] = 0;
                select[1] = 0;
            }
        });
        tvSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStringItemClick(select[0], select[1]);
                window.dismiss();
            }
        });

        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        ColorDrawable colorDrawable = new ColorDrawable(000000);
        window.setBackgroundDrawable(colorDrawable);
        window.setAnimationStyle(R.style.anim_window_select);
        window.showAsDropDown(locationView, ScreenUtils.getScreenWidth(context) - window.getWidth(), DensityUtils.dip2px(context, 1));
//        view.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                window.dismiss();
//            }
//        });
    }

    public interface OnStringItemClickListener {
        void onStringItemClick(int position, int sex);
    }


    private static class GridAdapter extends BaseAdapter {
        private Context context;
        private List<GoodsNameAndId> list;

        public GridAdapter(Context context, List<GoodsNameAndId> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.textitemadapter_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.text_item);
            textView.setText(list.get(position).getName());

            if (position == 0) textView.setSelected(true);
            return convertView;
        }
    }


}
