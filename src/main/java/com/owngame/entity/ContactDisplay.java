package com.owngame.entity;

/**
 * 联系人实体用于展示的字段
 * Created by Administrator on 2016-9-7.
 */

public class ContactDisplay {
    long base_id;// 基础信息表中的id
    long high_id;// 高级信息表中的id
    String groupname = "所在组";
    String name = "姓名";
    String title = "职务";
    String phone = "13988888888";
    String description = "备注";
    String grade = "0";// 等级
    String openid = null;// 微信openid
    String backup = null;// 备用信息

    public ContactDisplay() {
    }

    public ContactDisplay(ContactBase contactBase, ContactHigh contactHigh) {
        setBase_id(contactBase.getId());
        setGroupname(contactBase.getGroupname());
        setName(contactBase.getName());
        setTitle(contactBase.getTitle());
        setDescription(contactBase.getDescription());
        if (contactHigh != null) {
            setHigh_id(contactHigh.getId());
            setPhone(contactHigh.getPhone());
            setGrade(contactHigh.getGrade());
            setOpenid(contactHigh.getOpenid());
            setBackup(contactHigh.getBackup());
        }
    }

    public long getBase_id() {
        return base_id;
    }

    public void setBase_id(long base_id) {
        this.base_id = base_id;
    }

    public long getHigh_id() {
        return high_id;
    }

    public void setHigh_id(long high_id) {
        this.high_id = high_id;
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

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    @Override
    public String toString() {
        return "ContactDisplay{" +
                "base_id=" + base_id +
                ", high_id=" + high_id +
                ", groupname='" + groupname + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                ", grade='" + grade + '\'' +
                ", openid='" + openid + '\'' +
                ", backup='" + backup + '\'' +
                '}';
    }
}
