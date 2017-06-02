package com.cfkj.dushikuaibang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cfkj.dushikuaibang.Entity.Comment;
import com.cfkj.dushikuaibang.Entity.Message;
import com.cfkj.dushikuaibang.R;
import com.cfkj.dushikuaibang.Utils.TimeUtils;

import java.util.List;

/**
 * Created by fengm on 2017-4-29.
 */

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments;
    private List<Message> messages;
    private int type = 0;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public CommentAdapter(Context context, List<Message> messages, int type) {
        this.context = context;
        this.messages = messages;
        this.type = type;
    }

    @Override
    public int getCount() {

        return type == 0 ? comments.size() : messages.size();
    }

    @Override
    public Object getItem(int position) {
        return type == 0 ? comments.get(position) : messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.commentadapter_item, null, false);
            vh = new ViewHolder();
            vh.time = (TextView) convertView.findViewById(R.id.time);
            vh.name = (TextView) convertView.findViewById(R.id.name);
            vh.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (type == 0) {
            vh.name.setText(comments.get(position).getUser_name());
            vh.time.setText(TimeUtils.getFormatTime(comments.get(position).getAdd_time()));
            vh.content.setText(comments.get(position).getEvaluate_content());
        } else {
            vh.name.setText(messages.get(position).getUser_name());
            vh.time.setText(TimeUtils.getFormatTime(messages.get(position).getAdd_time()));
            vh.content.setText(messages.get(position).getContent());
        }
        return convertView;
    }

    class ViewHolder {
        TextView name, time, content;
    }

}
