package com.owngame.entity;

/**
 * 联系人实体
 * Created by Administrator on 2016-9-7.
 */
public class Contact {
    long id;
    String groupname;
    String name;
    String title;
    String phone;
    String description;

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

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", groupname='" + groupname + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
