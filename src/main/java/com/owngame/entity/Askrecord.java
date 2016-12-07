package com.owngame.entity;

import java.util.Date;

/**
 * 当有人主动发出询问请求时的记录
 * Created by Administrator on 2016-12-7.
 */
public class Askrecord {
    long id;
    String name;// 请求人姓名
    String phone;// 请求人的手机号
    int type;//0:手机查询,1: 微信查询
    Date time;// 查询时间
    String functions;// 查询的功能
    String description;// 描述
    int issuccess;// 是否成功

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }


    public int getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(int issuccess) {
        this.issuccess = issuccess;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Askrecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", functions='" + functions + '\'' +
                ", issuccess=" + issuccess +
                ", description='" + description + '\'' +
                '}';
    }
}
