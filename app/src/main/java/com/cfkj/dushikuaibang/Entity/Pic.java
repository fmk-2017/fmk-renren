package com.cfkj.dushikuaibang.Entity;

import java.util.List;

/**
 * Created by fengm on 2017-3-19.
 */

public class Pic {

    private List<String> ad_photo;
    private String ad_name;

    public Pic() {
    }

    public List<String> getAd_photo() {
        return ad_photo;
    }

    public void setAd_photo(List<String> ad_photo) {
        this.ad_photo = ad_photo;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }
}
