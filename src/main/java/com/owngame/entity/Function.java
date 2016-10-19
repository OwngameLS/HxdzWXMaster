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
    String usetype;// 由于加入了SQL语句查询，需要确定使用哪个 sql或rules
    String readfields;// 要读取的字段 用户查询所需的字段a,aName#b,bName
    String sortfields;// 排序字段，根据这个字段才能查询到最新的数据 A ASC,B DESC 默认为降序排列
    String fieldrules;// 规则字段，字段名，值，规则 根据规则来判断 a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#e,eName,bcde,NE#f,fName,xxxx,RG@12BT34
    String isreturn;// 读取结果是否返回的规则（由于需要涉及到预警功能，所以需要定义规则）
    // 由于功能是定时执行，因此不一定是每次都读取到数据就需要告知
    // anyway: 不论如何都返回
    // oncase: 监听几个字段，当其中一个字段达到报警要求时就需要告知
    String sqlstmt;//sql语句
    String sqlfields;// sql查询的字段属性，按照顺序来a,aName#b,bName

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

    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    public String getReadfields() {
        return readfields;
    }

    public void setReadfields(String readfields) {
        this.readfields = readfields;
    }

    public String getSortfields() {
        return sortfields;
    }

    public void setSortfields(String sortfields) {
        this.sortfields = sortfields;
    }

    public String getFieldrules() {
        return fieldrules;
    }

    public void setFieldrules(String fieldrules) {
        this.fieldrules = fieldrules;
    }

    public String getIsreturn() {
        return isreturn;
    }

    public void setIsreturn(String isreturn) {
        this.isreturn = isreturn;
    }

    public String getSqlstmt() {
        return sqlstmt;
    }

    public void setSqlstmt(String sqlstmt) {
        this.sqlstmt = sqlstmt;
    }

    public String getSqlfields() {
        return sqlfields;
    }

    public void setSqlfields(String sqlfields) {
        this.sqlfields = sqlfields;
    }
}
