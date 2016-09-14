package com.owngame.entity;

/**
 * 仅仅用来更新组名的类
 * Created by Administrator on 2016-9-13.
 */
public class GroupName {

    long id;
    String originalName;
    String newName;

    public GroupName() {
    }

    public GroupName(long id, String originalName, String newName) {
        this.id = id;
        this.originalName = originalName;
        this.newName = newName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
