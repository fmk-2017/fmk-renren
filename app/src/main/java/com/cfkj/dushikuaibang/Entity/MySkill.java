package com.cfkj.dushikuaibang.Entity;

/**
 * Created by fengm on 2017-4-27.
 */

public class MySkill {

    private String skill_id;
    private String category_id;
    private String user_id;
    private String server_lon;
    private String server_lat;
    private String server_name;
    private String server_time;
    private String skill_info;
    private String skill_photo;
    private String skill_price;
    private String addtime;
    private String status;
    private String praisesum;
    private String user_photo;
    private String score;
    private String cat_name;
    private String server_type_name;


    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getSkill_info() {
        return skill_info;
    }

    public void setSkill_info(String skill_info) {
        this.skill_info = skill_info;
    }

    public String getSkill_photo() {
        return skill_photo;
    }

    public void setSkill_photo(String skill_photo) {
        this.skill_photo = skill_photo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPraisesum() {
        return praisesum;
    }

    public void setPraisesum(String praisesum) {
        this.praisesum = praisesum;
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
}
