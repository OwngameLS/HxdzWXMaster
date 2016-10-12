package com.owngame.entity;

/**
 * Created by Administrator on 2016-9-27.
 */
public class Function {
    long id;
    String name;
    String description;
    String keywords;// 关键字，当用户自主查询时，通过关键字匹配
    String ip;
    String port;
    String dbtype;
    String dbname;
    String username;
    String password;
    String tablename;
    String sortfields;// 排序字段，根据这个字段才能查询到最新的数据 A ASC,B DESC 默认为降序排列
    String fields;// a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#,e,eName,bcde,NE#f,fName,cdef,RG@12BT34
    // 字段，字段名，值，规则 根据规则来判断
    String rules;// 读取结果是否返回的规则（由于需要涉及到预警功能，所以需要定义规则）
    // 由于功能是定时执行，因此不一定是每次都读取到数据就需要告知
    // anyway: 不论如何都返回
    // oncase: 监听几个字段，当其中一个字段达到报警要求时就需要告知

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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

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

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getSortfields() {
        return sortfields;
    }

    public void setSortfields(String sortfields) {
        this.sortfields = sortfields;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
