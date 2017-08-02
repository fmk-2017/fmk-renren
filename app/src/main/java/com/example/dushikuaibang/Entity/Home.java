package com.example.dushikuaibang.Entity;

import java.util.List;

/**
 * Created by fengm on 2017-3-19.
 */

public class Home {
    private List<Pic> home_pic;
    private List<HomeCategory> get_category;
    private List<Skill> get_server_list;

    public Home() {
    }

    public List<Pic> getHome_pic() {
        return home_pic;
    }

    public void setHome_pic(List<Pic> home_pic) {
        this.home_pic = home_pic;
    }

    public List<HomeCategory> getGet_category() {
        return get_category;
    }

    public void setGet_category(List<HomeCategory> get_category) {
        this.get_category = get_category;
    }

    public List<Skill> getGet_server_list() {
        return get_server_list;
    }

    public void setGet_server_list(List<Skill> get_server_list) {
        this.get_server_list = get_server_list;
    }
}
