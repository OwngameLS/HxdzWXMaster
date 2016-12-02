package com.owngame.entity;

/**
 * 联系人基础信息表
 * Created by Administrator on 2016-11-30.
 */
public class ContactBase {
    long id;
    long highid;// 关联的高级信息的id（外键）
    String groupname = "所在组";
    String name = "姓名";
    String title = "职务";
    String description = "备注";


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getHighid() {
        return highid;
    }

    public void setHighid(long highid) {
        this.highid = highid;
    }
}
