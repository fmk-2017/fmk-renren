package com.example.dushikuaibang.Entity;

/**
 * Created by fengm on 2017-4-29.
 */

public class Message {

    private String message_id;
    private String merchant_id;
    private String user_id;
    private String user_name;
    private String content;
    private String add_time;
    private String nickname;

    //{"message_id":"27","merchant_id":"33","user_id":"19","user_name":"15827991714","content":"OL肉筋肉","add_time":"1498241311","nickname":"15827991714"}

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
