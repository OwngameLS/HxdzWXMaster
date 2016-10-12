package com.owngame.service.impl;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.Function;
import com.owngame.entity.FunctionField;
import com.owngame.service.FunctionService;
import com.owngame.utils.DBUtil;
import com.owngame.utils.FunctionFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.result.FlashAttributeResultMatchers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
     * @param function
     * @return
     */
    public boolean canConnect(Function function){
        Connection connection = DBUtil.createConn(function);
        if(connection != null){
            DBUtil.close(connection);
            return true;
        }else{
            return false;
        }
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
        ArrayList<FunctionField> functionFields = FunctionFieldUtil.parseFieldsString(function.getFields());
        // 根据要获取的表和字段名，构造查询语句
        String sql = getQueryStatement(function.getTablename(), function.getSortfields(), functionFields);
        // 查询并得到结果
        return doQuery(connection, sql, functionFields, function.getRules());
    }

    /**
     * 构造查询语句
     *
     * @param tableName
     * @param sortfields
     * @param functionFields
     * @return
     */
    private String getQueryStatement(String tableName, String sortfields, ArrayList<FunctionField> functionFields) {
        String sql = "select ";
        for (int i = 0; i < functionFields.size(); i++) {
            sql = sql + functionFields.get(i).getField();
            if ((i + 1) < functionFields.size()) {
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
        sql = sql + ";";
        return sql;
    }

    /**
     * 做查询
     *
     * @param conn
     * @param sql
     * @param functionFields
     * @return
     */
    private String doQuery(Connection conn, String sql, ArrayList<FunctionField> functionFields, String resultrules) {
        System.out.println("sql: " + sql);
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        String result = "";
        try {
            rs = ps.executeQuery();
            if (rs.next()) {// 只选第一条就行
                if (resultrules.equals("anyway")) {// 当返回要求为必须返回时
                    for (FunctionField functionField : functionFields) {
                        result = result + functionField.getFieldName() + ":" + rs.getObject(functionField.getField()) + ";";
                    }
                } else if (resultrules.equals("oncase")) {// 根据规则来处理查询结果
                    ArrayList<Integer> isReturns = new ArrayList<Integer>();
                    ArrayList<String> values = new ArrayList<String>();
                    for (FunctionField functionField : functionFields) {
                        boolean isReturn = false;// 是否返回查询结果
                        if (resultrules.equals("oncase")) {// 根据条件返回
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
