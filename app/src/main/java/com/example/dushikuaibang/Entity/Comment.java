package com.example.dushikuaibang.Entity;

/**
 * Created by fengm on 2017-4-29.
 */

public class Comment {


    private String evaluate_id;
    private String evaluate_content;
    private String score;
    private String server_user_id;
    private String evaluate_user_id;
    private String add_time;
    private String skill_id;
    private String server_id;
    private String user_name;
    private String nickname;

    //{"message_id":"27","merchant_id":"33","user_id":"19","user_name":"15827991714","content":"OL肉筋肉","add_time":"1498241311","nickname":"15827991714"}
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEvaluate_id() {
        return evaluate_id;
    }

    public void setEvaluate_id(String evaluate_id) {
        this.evaluate_id = evaluate_id;
    }

    public String getEvaluate_content() {
        return evaluate_content;
    }

    public void setEvaluate_content(String evaluate_content) {
        this.evaluate_content = evaluate_content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getServer_user_id() {
        return server_user_id;
    }

    public void setServer_user_id(String server_user_id) {
        this.server_user_id = server_user_id;
    }

    public String getEvaluate_user_id() {
        return evaluate_user_id;
    }

    public void setEvaluate_user_id(String evaluate_user_id) {
        this.evaluate_user_id = evaluate_user_id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}
