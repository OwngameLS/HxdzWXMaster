package com.owngame.entity;

/**
 * 字段和其自定义名称 a,aName#b,bName
 * Created by Administrator on 2016-10-19.
 */
public class FieldAndSelfName {
    String field;// 字段名（数据库中的名称)
    String selfName;// 自定义名称

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSelfName() {
        return selfName;
    }

    public void setSelfName(String selfName) {
        this.selfName = selfName;
    }
}
