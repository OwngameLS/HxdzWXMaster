package com.owngame.entity;

/**
 * Created by Administrator on 2017/2/28.
 * 自定义关键字
 */
public class Quickanswer {
    long id;
    String keyname; // 关键字名
    String result;// 返回的结果
    String description;// 描述信息
    int enable = 1;// 0 不启用，1启用

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
