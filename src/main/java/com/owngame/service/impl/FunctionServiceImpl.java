package com.owngame.service.impl;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.*;
import com.owngame.service.FunctionService;
import com.owngame.utils.DBUtil;
import com.owngame.utils.FunctionFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016-9-27.
 */
@Service
public class FunctionServiceImpl implements FunctionService {
    @Autowired
    FunctionDao functionDao;

    public int createFunction(Function function) {
        return functionDao.insert(function);
    }

    public ArrayList<Function> queryAll() {
        return functionDao.queryAll();
    }

    public int deleteById(long id) {
        return functionDao.deleteById(id);
    }

    public int update(Function function) {
        return functionDao.update(function);
    }

    public Function getByName(String name) {
        return functionDao.queryByName(name);
    }

    public Function getByKeywords(String keywords) {
        return functionDao.queryByKeywords(keywords);
    }

    public Function getById(long id) {
        return functionDao.queryById(id);
    }

    /**
     * 查询是否连接的上指定数据库
     * 如果查询到了就返回该表包含的字段
     * @param function
     * @return
     */
    public ArrayList<String> testConnect(Function function){
        Connection connection = DBUtil.createConn(function);
        if(connection != null){
            // 连接上了就读取该表的字段属性
            ArrayList<String> colNames = getTableInfos(connection, function.getTablename());
            DBUtil.close(connection);
            return colNames;
        }else{
            return null;
        }
    }

    /**
     * 获取表字段
     * @param tablename
     */
    private ArrayList<String> getTableInfos(Connection conn, String tablename){
        String sql="select * from "+ tablename + ";";
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        ArrayList<String> colNames = new ArrayList<String>();
        try {
            rs=ps.executeQuery();
            ResultSetMetaData meta=rs.getMetaData();
            for(int i=1;i<=meta.getColumnCount();i++){
                colNames.add(meta.getColumnName(i));//获取字段名
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return colNames;
    }


    /**
     * 查询得到该功能的结果（最复杂的方法）
     * @param function
     * @return
     */
    public String getFunctionResult(Function function) {
        // 1.现根据function获得服务组件
        // 获得数据库链接
        Connection connection = DBUtil.createConn(function);
        ArrayList<FieldAndSelfName> readFields = FunctionFieldUtil.parseFieldSelfName(function.getReadfields());// 所需获取的字段
        ArrayList<FunctionFieldRule> fieldRules = FunctionFieldUtil.parseFieldsString(function.getFieldrules());// 判断规则的字段
        // 根据要获取的表和字段名，构造查询语句
        String sql = getQueryStatement(function.getTablename(), function.getSortfields(), readFields, fieldRules);
        // 查询并得到结果
        return doQuery(connection, sql, readFields);
//        return doQuery(connection, sql, readFields, fieldRules, function.getIsreturn());
    }


    /**
     * 检查Sql语句
     * @param sql
     * @return
     */
    public FunctionSqlResult checkSql(Function function, String sql) {
        FunctionSqlResult functionSqlResult = new FunctionSqlResult();
        // 1.现根据function获得服务组件
        // 获得数据库链接
        Connection conn = DBUtil.createConn(function);
        if(conn != null){// 连接上了
            if(sql.endsWith(";") == false){// 可能忘了写分号
                sql = sql + ";";
            }
            PreparedStatement ps = DBUtil.prepare(conn, sql);
            ResultSet rs = null;
            ArrayList<String> fields = new ArrayList<String>();
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    functionSqlResult.setIsSuccess(1);
                    ResultSetMetaData meta = rs.getMetaData();
                    for(int i=1;i<=meta.getColumnCount();i++){ // 序号从1开始
                        fields.add(meta.getColumnName(i));//获得字段名
                    }
                    functionSqlResult.setFields(fields);
                }
                DBUtil.close(rs);
            } catch (Exception e) {
                e.printStackTrace();
                functionSqlResult.setIsSuccess(-1);
                fields.add(e.getMessage());// 添加错误信息
                functionSqlResult.setFields(fields);
                DBUtil.close(ps);
                DBUtil.close(conn);
                return functionSqlResult;
            }
            DBUtil.close(ps);
            DBUtil.close(conn);
        }else {
            functionSqlResult.setIsSuccess(-1);
        }
        return functionSqlResult;
    }

    /**
     * 检查关键字
     * @param keywords
     * @return
     */
    public FunctionKeywordsResult checkKeywords(long id, String keywords) {
        FunctionKeywordsResult functionKeywordsResult = new FunctionKeywordsResult();
        if(id > 0){// 是更新原来的功能，检测其关键字是否更改了
            // 通过id查询原来的关键字
            Function function = functionDao.queryById(id);
            if(keywords.equals(function.getKeywords())){// 与原来的一样，没有改变
                functionKeywordsResult.setIsSuccess(1);
                return functionKeywordsResult;
            }
        }
        String keys[] = keywords.split(",");// 分割关键字组合
        HashMap<String, String> similarKeys = new HashMap<String, String>();
        for(int i=0;i<keys.length;i++){
            ArrayList<Function> rr = functionDao.checkKeywords("%" + keys[i] + "%");// 模糊查询
            if(rr != null){
                for(int j=0;j<rr.size();j++){
                    String formerKeys[] = rr.get(j).getKeywords().split(",");
                    if(id < 0){//新建
                        for(String fk : formerKeys){
                            if(fk.equals(keys[i])){// 关键字重复
                                functionKeywordsResult.setIsSuccess(-1);//有重复的了
                                similarKeys = putKeyswords(similarKeys, keys[i], fk, true);
                            }else if(fk.contains(keys[i])){// 关键字有类似的
                                similarKeys = putKeyswords(similarKeys, keys[i], fk, false);
                            }
                        }
                    }else{// 更新
                        long formerId = rr.get(j).getId();
                        for(String fk : formerKeys){
                            if(fk.equals(keys[i])){// 关键字重复
                                if(formerId != id){// 不是原来的关键字
                                    functionKeywordsResult.setIsSuccess(-1);//有重复的了
                                    similarKeys = putKeyswords(similarKeys, keys[i], fk, true);
                                }
                            }else if(fk.contains(keys[i])){// 关键字有类似的
                                // 更新时发现类似的关键字，不用管
                            }
                        }

                    }
                }
            }
        }

        if(functionKeywordsResult.getIsSuccess() < 0 ){// 需要返回错误信息
            ArrayList<String> keysInfo = new ArrayList<String>();
            Iterator iter = similarKeys.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                keysInfo.add((String) entry.getValue());
            }
            functionKeywordsResult.setSimlarKeys(keysInfo);
        }
        return functionKeywordsResult;
    }

    // 将关键字重复信息进行整理
    private HashMap<String, String> putKeyswords(HashMap<String, String> similarKeys, String key, String value, boolean isEqual){
        String v = "";
        if(similarKeys.containsKey(key)) {// 原来已经添加了这个key的对象
            v = similarKeys.get(key);
        }
        if(isEqual){// 关键字重复的只会查询到一次 TODO 前台要避免出现 keyA,keyA这样的关键字组合
            v = "关键字"+key+"已存在，类似的还有：" + v;
        }else{
            v = v +"[" +value + "] ";
        }
        similarKeys.put(key, v);
        return similarKeys;
    }


    /**
     * 构造查询语句
     *
     * @param tableName
     * @param sortfields
     * @param readFields
     * @param fieldRules
     * @return
     */
    private String getQueryStatement(String tableName, String sortfields, ArrayList<FieldAndSelfName> readFields, ArrayList<FunctionFieldRule> fieldRules) {
        String sql = "select ";
        for (int i = 0; i < readFields.size(); i++) {
            sql = sql + readFields.get(i).getField();
            if ((i + 1) < readFields.size()) {
                sql = sql + ",";
            }
        }
        sql = sql + " from " + tableName + " order by ";
        String sortF[] = sortfields.split(",");
        for (int i = 0; i < sortF.length; i++) {
            sql = sql + sortF[i];
            if ((i + 1) < sortF.length) {
                sql = sql + ",";
            }
        }
        sql = sql + parseFieldRulesToWhereStmt(fieldRules) +";";
        return sql;
    }


    /**
     * 将字段规则转换为where子句
     * @param fieldRules
     * @return
     */
    private String parseFieldRulesToWhereStmt(ArrayList<FunctionFieldRule> fieldRules){
        String whereStmt= " where ";
        if(fieldRules.size() == 0){
            whereStmt = "";
        }else{
            for(int i=0;i<fieldRules.size();i++){
                // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#,e,eName,bcde,NE#f,fName,xxx,RG@12BT34
                FunctionFieldRule fieldRule = fieldRules.get(i);

                if(fieldRule.getRule().equals("BB")){
                    whereStmt = whereStmt + "(" + fieldRule.getField() + " > " + fieldRule.getCompareValue() + ")";
                }else if(fieldRule.getRule().equals("LL")){
                    whereStmt = whereStmt + "(" + fieldRule.getField() + " < " + fieldRule.getCompareValue() + ")";
                }else if(fieldRule.getRule().equals("EQ")){
                    if(isNum(fieldRule.getCompareValue())){
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " = " + fieldRule.getCompareValue() + ")";
                    }else{
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " = '" + fieldRule.getCompareValue() + "')";
                    }
                }else if(fieldRule.getRule().equals("NE")){
                    if(isNum(fieldRule.getCompareValue())) {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " != " + fieldRule.getCompareValue() + ")";
                    }else {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " != '" + fieldRule.getCompareValue() + "')";
                    }
                }else if(fieldRule.getRule().startsWith("RG")){
                    String rgRule = fieldRule.getRule();
                    String below,above;
                    int indexOfB, indexOfO, indexOfT, indexOfAt;
                    indexOfAt = rgRule.indexOf("@");
                    if(rgRule.contains("BT")){
                        indexOfB = rgRule.indexOf("B");
                        indexOfT = rgRule.indexOf("T");
                        below = rgRule.substring(indexOfAt+1,indexOfB);
                        above = rgRule.substring(indexOfT+1);
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " > " + below + " and " + fieldRule.getField() + " < " + above +")";
                    }else if(rgRule.contains("OUT")){
                        indexOfO = rgRule.indexOf("O");
                        indexOfT = rgRule.indexOf("T");
                        below = rgRule.substring(indexOfAt+1,indexOfO);
                        above = rgRule.substring(indexOfT+1);
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " < " + below + " or " + fieldRule.getField() + " > " + above +")";
                    }
                }else if(fieldRule.getRule().equals("NN")){
                    continue;
                }
                if((i+1) < fieldRules.size()){
                    whereStmt = whereStmt + " and ";
                }
            }
        }
        return whereStmt;
    }


    /**
     * 根据sql语句查询
     * @param conn
     * @param sql
     * @param readFields
     * @return
     */
    private String doQuery(Connection conn, String sql, ArrayList<FieldAndSelfName> readFields){
        System.out.println("sql: " + sql);
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        String result = "";
        try {
            rs = ps.executeQuery();
            if (rs.next()) {// 只选第一条就行
                for (int i=0;i< readFields.size();i++){
                    FieldAndSelfName fieldAndSelfName = readFields.get(i);
                    result = result + fieldAndSelfName.getSelfName() + ":" + rs.getObject(fieldAndSelfName.getField());
                    if((i+1)<readFields.size()){
                        result = result + ";";
                    }
                }
            }
            DBUtil.close(rs);
        } catch (Exception e) {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        DBUtil.close(ps);
        DBUtil.close(conn);
        return result;
    }


    /**
     * 做查询
     *
     * @param conn
     * @param sql
     * @param fieldrules
     * @return
     */
    private String doQuery(Connection conn, String sql, ArrayList<FieldAndSelfName> readFields, ArrayList<FunctionFieldRule> fieldrules, String isreturn) {
        System.out.println("sql: " + sql);
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        String result = "";
        try {
            rs = ps.executeQuery();
            if (rs.next()) {// 只选第一条就行
                if (isreturn.equals("anyway")) {// 当返回要求为必须返回时
                    for (FieldAndSelfName fieldAndSelfName : readFields) {
                        result = result + fieldAndSelfName.getSelfName() + ":" + rs.getObject(fieldAndSelfName.getField()) + ";";
                    }
                } else if (isreturn.equals("oncase")) {// 根据规则来处理查询结果
                    ArrayList<Integer> isReturns = new ArrayList<Integer>();
                    ArrayList<String> values = new ArrayList<String>();
                    for (FunctionFieldRule functionField : fieldrules) {
                        boolean isReturn = false;// 是否返回查询结果
                        if (isreturn.equals("oncase")) {// 根据条件返回
                            // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,NE@V
                            // 从数据库读到的数据
                            String value = (String) rs.getObject(functionField.getField());
                            // 判断规则
                            String rule = functionField.getRule();
                            if (rule.startsWith("NN")) {// 不需要判断
                                isReturns.add(0);
                                values.add(value);
                            } else {// 需要判断
                                if (isNum(value)) {
                                    boolean isInt = true;
                                    int intValue = 0, intCompareValue = 0;
                                    float floatValue = 0.0f, floatCompareValue = 0.0f;
                                    if (value.contains(".")) {// 判断是否为整数
                                        isInt = false;
                                    }
                                    if (isInt) {
                                        intValue = Integer.parseInt(value);
                                        intCompareValue = Integer.parseInt(functionField.getCompareValue());
                                    } else {
                                        floatValue = Float.parseFloat(value);
                                        floatCompareValue = Float.parseFloat(functionField.getCompareValue());
                                    }
                                    if (rule.equals("BB")) {// 大于给定值
                                        if (isInt) {
                                            if (intValue > intCompareValue) {
                                                isReturn = true;
                                            }
                                        } else {
                                            if (floatValue > floatCompareValue) {
                                                isReturn = true;
                                            }
                                        }
                                        if(isReturn){
                                            value = functionField.getFieldName()+":"+value+",大于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.equals("LL")) {// 小雨给定值
                                        if (isInt) {
                                            if (intValue < intCompareValue) {
                                                isReturn = true;
                                            }
                                        } else {
                                            if (floatValue < floatCompareValue) {
                                                isReturn = true;
                                            }
                                        }
                                        if(isReturn){
                                            value = functionField.getFieldName()+":"+value+",小于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.startsWith("EQ")) { // 等于给定值
                                        if (isInt) {
                                            if (intValue == intCompareValue) {
                                                isReturn = true;
                                            }
                                        } else {
                                            if (floatValue == floatCompareValue) {
                                                isReturn = true;
                                            }
                                        }
                                        if(isReturn){
                                            value = functionField.getFieldName()+":"+value+",等于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.startsWith("NE")) { // 不等于给定值
                                        if (isInt) {
                                            if (intValue != intCompareValue) {
                                                isReturn = true;
                                            }
                                        } else {
                                            if (floatValue != floatCompareValue) {
                                                isReturn = true;
                                            }
                                        }
                                        if(isReturn){
                                            value = functionField.getFieldName()+":"+value+",不等于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if(rule.startsWith("RG")){// 范围条件 range
                                        // RG@12BT12 RG@12OUT12 规则示例
                                        // 12BT15:比给定值上浮动15，下浮12;12OUT15:比给定值 下浮超过12 或者 上浮超过15
                                        // 先找出两个范围值
                                        String ts = rule.split("@")[1];
                                        String ranges[] = null;
                                        if(ts.contains("BT")){
                                            ranges = ts.split("BT");
                                        }else if(ts.contains("OUT")){
                                            ranges = ts.split("OUT");
                                        }
                                        // 为了简化逻辑，都转化成Float来处理
                                        floatValue = Float.parseFloat(value);
                                        floatCompareValue = Float.parseFloat(functionField.getCompareValue());
                                        // 下限
                                        float downLimit = floatCompareValue - Float.parseFloat(ranges[0]);
                                        // 上限
                                        float upLimit = floatCompareValue + Float.parseFloat(ranges[1]);

                                        if(ts.contains("BT")){// 中间范围
                                            if(floatValue>=downLimit && floatValue<=upLimit){// 满足规则
                                                value = functionField.getFieldName()+":"+value+",在设定范围内（"+downLimit+","+upLimit+");";
                                                isReturns.add(1);
                                                values.add(value);
                                                isReturn = true;
                                            }
                                        }else if(ts.contains("OUT")){// 两头范围
                                            if(floatValue <= downLimit){
                                                value = functionField.getFieldName()+":"+value+",低于常规范围（-,"+downLimit+");";
                                                isReturns.add(1);
                                                values.add(value);
                                                isReturn = true;
                                            } else if(floatValue >= floatCompareValue){
                                                value = functionField.getFieldName()+":"+value+",高于常规范围（"+upLimit+",+);";
                                                isReturns.add(1);
                                                values.add(value);
                                                isReturn = true;
                                            }
                                        }
                                    }
                                } else {// 不是数字
                                    if (rule.startsWith("EQ")) { // 等于给定值
                                        if (value.contains(functionField.getCompareValue())) {
                                            isReturn = true;
                                            value = functionField.getFieldName()+":"+value+",等于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.startsWith("NE")) { // 不等于给定值
                                        if (value.contains(functionField.getCompareValue()) == false) {
                                            isReturn = true;
                                            value = functionField.getFieldName()+":"+value+",不等于给定值"+functionField.getCompareValue()+";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    }
                                }
                                if (isReturn == false) {// 根据规则判断之后 需要返回
                                    value = functionField.getFieldName()+":"+value+";";
                                    isReturns.add(0);
                                    values.add(value);
                                }
                            }
                        }
                    }
                    // 判断查询的字段里是否有需要返回的，当一个字段需要返回时，就将所有的都返回了
                    if(isReturns.contains(new Integer(1))){
                        for(String s : values){
                            result = result + s;
                        }
                    }else{
                        result = "NOTNEED";// 不需要返回
                    }
                }
//                result = result + "#";// 该次查询结束结尾符号
            } else {
                result = "NONE";// 未查询到
            }
            DBUtil.close(rs);
        } catch (Exception e) {
            DBUtil.close(ps);
            DBUtil.close(conn);
        }
        DBUtil.close(ps);
        DBUtil.close(conn);
        return result;
    }


    // 判断字符串是不是数字
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

}
