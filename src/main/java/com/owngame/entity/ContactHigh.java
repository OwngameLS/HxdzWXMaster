package com.owngame.entity;

/**
 * 联系人高级信息 包括微信等信息
 * Created by Administrator on 2016-11-30.
 */
public class ContactHigh {
    long id;
    String phone = "13988888888";
    String grade = "0";// 等级
    String openid = null;// 微信openid
    String backup = null;// 备用信息

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }
}
