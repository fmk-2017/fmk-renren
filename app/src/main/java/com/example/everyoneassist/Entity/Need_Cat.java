package com.example.everyoneassist.Entity;

import java.util.List;

/**
 * Created by fengm on 2017-3-20.
 */

public class Need_Cat {

    private String id;
    private String name;
    private String cat_id;
    private String cat_name;
    private List<String> item;

    public Need_Cat() {
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }
}
