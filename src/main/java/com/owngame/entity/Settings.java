package com.owngame.entity;

/**
 * 设置项
 * Created by Administrator on 2016-12-29.
 */
public class Settings {
    long id;
    String description;// 描述名
    String name;// 设置名称
    String value = "no";// 设置值
    String referto = "no";// 依赖的Settings

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReferto() {
        return referto;
    }

    public void setReferto(String referto) {
        this.referto = referto;
    }
}
