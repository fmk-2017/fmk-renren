package com.example.dushikuaibang.Entity;

/**
 * Created by Administrator on 2017/3/30.
 */

public class GoodsNameAndId {
    private String name;
    private int cat_id;

    public GoodsNameAndId(String name, int cat_id) {
        this.name = name;
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
}
