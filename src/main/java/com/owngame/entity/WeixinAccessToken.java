package com.owngame.entity;

/**
 * Created by Administrator on 2016/12/24.
 */
public class WeixinAccessToken {
    private int id;
    private String accesstoken;
    private String expiresin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(String expiresin) {
        this.expiresin = expiresin;
    }
}
