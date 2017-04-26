package com.example.everyoneassist.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.everyoneassist.R;


/**
 * Created by fengm on 2017/2/17.
 */

public class TextItemAdapter extends BaseAdapter {

    private Context context;
    private String[] array;

    public TextItemAdapter(Context context, String[] array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length;
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
//        ViewHolder vholder;
//        if (convertView == null) {
//            vholder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.textitemadapter_item, null, false);
        TextView textView = (TextView) convertView.findViewById(R.id.text_item);
//            convertView.setTag(vholder);
//        } else vholder = (ViewHolder) convertView.getTag();

        textView.setText(array[position]);
        if (position == 0) textView.setSelected(true);
        return convertView;
    }

//    class ViewHolder {
//        TextView textView;
//    }
}
