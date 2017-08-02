package com.example.dushikuaibang.Entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengm on 2017-3-10.
 */

public class Skill {

    private static Skill skill;
    private String skill_id;
    private String category_id;
    private String user_id;
    private String server_lon;
    private String server_lat;
    private String server_name;
    private String server_time;
    private String status;
    private String skill_info;
    private String skill_photo;
    private List<String> skill_photos;
    private String skill_price;
    private String addtime;
    private String nickname;
    private String user_name;
    private String user_photo;
    private String cat_name;
    private String server_type_name;
    private String range;
    private String commentNum;
    private int praisesum;
    private int is_praise;

    public Skill() {
    }

    public static Skill getInstance() {
        if (skill == null)
            skill = new Skill();
        return skill;
    }

    public static Skill getNewInstance() {
        skill = new Skill();
        return skill;
    }

    public int getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(int is_praise) {
        this.is_praise = is_praise;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public List<String> getSkill_photos() {
        return skill_photos;
    }

    public void setSkill_photos(List<String> skill_photos) {
        this.skill_photos = skill_photos;
    }

    public String getSkill_photo() {
        return skill_photo;
    }

    public void setSkill_photo(String skill_photo) {
        this.skill_photo = skill_photo;

        if (TextUtils.isEmpty(skill_photo)) return;
        this.skill_photos = new ArrayList<String>();
        String str[] = skill_photo.split(",");
        for (String skill_p : str) {
            this.skill_photos.add(skill_p);
        }
    }

    public String getServer_lon() {
        return server_lon;
    }

    public void setServer_lon(String server_lon) {
        this.server_lon = server_lon;
    }

    public String getServer_lat() {
        return server_lat;
    }

    public void setServer_lat(String server_lat) {
        this.server_lat = server_lat;
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSkill_info() {
        return skill_info;
    }

    public void setSkill_info(String skill_info) {
        this.skill_info = skill_info;
    }

    public String getSkill_price() {
        return skill_price;
    }

    public void setSkill_price(String skill_price) {
        this.skill_price = skill_price;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getServer_type_name() {
        return server_type_name;
    }

    public void setServer_type_name(String server_type_name) {
        this.server_type_name = server_type_name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getPraisesum() {
        return praisesum;
    }

    public void setPraisesum(int praisesum) {
        this.praisesum = praisesum;
    }
}
