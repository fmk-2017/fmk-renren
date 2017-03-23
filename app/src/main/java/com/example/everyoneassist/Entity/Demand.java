package com.example.everyoneassist.Entity;

/**
 * Created by fengm on 2017-3-20.
 */

public class Demand {

    private String id;
    private String user_id;
    private String category_id;
    private String server_sex;
    private String server_day;
    private String server_type;
    private String server_lon;
    private String server_lat;
    private String server_price;
    private String server_tag;
    private String address;
    private String info;
    private String status;
    private String addtime;
    private String user_photo;
    private String nickname;
    private String user_name;
    private String cat_name;

    public Demand() {
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

    public String getServer_price() {
        return server_price;
    }

    public void setServer_price(String server_price) {
        this.server_price = server_price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getServer_sex() {
        return server_sex;
    }

    public void setServer_sex(String server_sex) {
        this.server_sex = server_sex;
    }

    public String getServer_day() {
        return server_day;
    }

    public void setServer_day(String server_day) {
        this.server_day = server_day;
    }

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public String getServer_tag() {
        return server_tag;
    }

    public void setServer_tag(String server_tag) {
        this.server_tag = server_tag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
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

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}
