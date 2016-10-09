package com.owngame.entity;

/**
 * Created by Administrator on 2016-9-27.
 */
public class Function {
    long id;
    String name;
    String description;
    String ip;
    String port;
    String databaseType;
    String tableName;
    String fields;// a,-1,NN#b,5,BB#c,200,LL#d,abcd,NE@V
    // 字段，值，规则 根据规则来判断
    // 当规则为NN 表示没有规则不报警
    // 当规则为BB 表示大于值时报警
    // 当规则为BE 表示大于等于值时报警
    // 当规则为LL 表示小于值时报警
    // 当规则为LE 表示小于等于值时报警
    // 当规则为NE@V 表示不等于值(在V范围内)时报警

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
