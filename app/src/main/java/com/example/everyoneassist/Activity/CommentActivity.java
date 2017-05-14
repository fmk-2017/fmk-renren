package com.example.everyoneassist.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.everyoneassist.Adapter.CommentAdapter;
import com.example.everyoneassist.Entity.Comment;
import com.example.everyoneassist.Entity.Message;
import com.example.everyoneassist.R;
import com.example.everyoneassist.Utils.HttpPostRequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CommentActivity extends BaseActivity implements HttpPostRequestUtils.HttpPostRequestCallback {

    private final String MESSAGE_LIST = "message_list";
    private final String COMMENT_LIST = "comment_list";
    private ListView commentListView;

    private String skill_id, userid;
    private List<Comment> comments;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        skill_id = getIntent().getStringExtra("skill_id");


        initHeader(TextUtils.isEmpty(skill_id) ? "留言" : "评论");

        userid = getIntent().getStringExtra("user_id");

        commentListView = (ListView) this.findViewById(R.id.commentListView);

        getData();

    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(skill_id)) map.put("act", MESSAGE_LIST);
        else map.put("act", COMMENT_LIST);
        map.put("skill_id", skill_id);
        map.put("user_id", userid);
        HttpPostRequestUtils.getInstance(this).Post(map);
    }

    @Override
    public void Success(String method, JSONObject json) throws JSONException {
        if (COMMENT_LIST.equals(method)) {
            comments = JSON.parseArray(json.getString("data"), Comment.class);
            commentListView.setAdapter(new CommentAdapter(this, comments));
        } else if (MESSAGE_LIST.equals(method)) {
            messages = JSON.parseArray(json.getString("data"), Message.class);
            commentListView.setAdapter(new CommentAdapter(this, messages, 1));
        }
    }

    @Override
    public void Fail(String method, String error) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
