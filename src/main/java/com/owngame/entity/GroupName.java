package com.owngame.entity;

/**
 * 仅仅用来更新组名的类
 * Created by Administrator on 2016-9-13.
 */
public class GroupName {
    String originalName;
    String newName;

    public GroupName() {
    }

    public GroupName(String originalName, String newName) {
        this.originalName = originalName;
        this.newName = newName;
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
