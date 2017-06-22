package com.cfkj.dushikuaibang.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/4.
 */

public class OrderBean implements Serializable {
    String id;
    String user_id;
    String category_id;
    String server_sex;
    String server_day;
    String server_type;
    String server_lon;
    String server_lat;
    String server_price;
    String server_tag;
    String address;
    String info;
    String is_accomplish;
    String status;
    String addtime;
    String cat_name;
    String cat_id;
    String count;
    String robsum;
    String server_type_name;

    public OrderBean() {
    }


    public String getRobsum() {
        return robsum;
    }

    public void setRobsum(String robsum) {
        this.robsum = robsum;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public String getServer_tag() {
        return server_tag;
    }

    public void setServer_tag(String server_tag) {
        this.server_tag = server_tag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_accomplish() {
        return is_accomplish;
    }

    public void setIs_accomplish(String is_accomplish) {
        this.is_accomplish = is_accomplish;
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

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getServer_type_name() {
        return server_type_name;
    }

    public void setServer_type_name(String server_type_name) {
        this.server_type_name = server_type_name;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", server_sex='" + server_sex + '\'' +
                ", server_day='" + server_day + '\'' +
                ", server_type='" + server_type + '\'' +
                ", server_lon='" + server_lon + '\'' +
                ", server_lat='" + server_lat + '\'' +
                ", server_price='" + server_price + '\'' +
                ", server_tag='" + server_tag + '\'' +
                ", address='" + address + '\'' +
                ", info='" + info + '\'' +
                ", status='" + status + '\'' +
                ", addtime='" + addtime + '\'' +
                ", cat_name='" + cat_name + '\'' +
                ", server_type_name='" + server_type_name + '\'' +
                '}';
    }
}
