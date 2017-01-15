package com.owngame.service.impl;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.*;
import com.owngame.service.ContactBaseService;
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
    public static final int QUESTIONTYPE_FUNCTION_ID = 0, QUESTIONTYPE_FUNCTION_NAME = 1, QUESTIONTYPE_FUNCTION_KEYWORDS = 2;

    @Autowired
    FunctionDao functionDao;
    @Autowired
    ContactBaseService contactBaseService;

    // 判断字符串是不是数字
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public int createFunction(Function function) {
        return functionDao.insert(function);
    }

    public ArrayList<Function> queryAll() {
        return functionDao.queryAll();
    }

    public ArrayList<Function> queryAllUsable() {
        return functionDao.queryAllUsable();
    }

    public String queryAllWithGrade(ContactHigh contactHigh, int type) {
        ArrayList<Function> functions = queryAll();
        String result = "";
        int g = Integer.parseInt(contactHigh.getGrade());
        for (int i = 0; i < functions.size(); i++) {
            Function tfunction = functions.get(i);
            int tg = Integer.parseInt(tfunction.getGrade());
            result += "功能名称：" + tfunction.getName()
                    + "，关键字：" + tfunction.getKeywords()
                    + "，描述：" + tfunction.getDescription()
                    + "，备注：";
            if (tg > g) {
                result += "您的权限级别不能使用此功能。";
            } else {
                result += "无。";
            }
            result += "\n";
        }
//        createAskrecord(contactHigh, type, "询问关键字提示", "询问关键字提示", 1);
        return result;
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
        // 不会在SQL语句中筛选，先将所有包含关键字的方法都找
        ArrayList<Function> functions = functionDao.queryByKeywords("%" + keywords + "%");
        if (functions == null || functions.size() == 0) {
            Function function = new Function();// id 为-1，说明不可用
            function.setKeywords(keywords);
            function.setDescription(null);
            return function;
        }
        // 由于function 关键字不会重复，找到就返回
        String similarKeys = "";
        for (int i = 0; i < functions.size(); i++) {
            similarKeys += functions.get(i).getKeywords();
            if ((i + 1) < functions.size()) {
                similarKeys += ",";
            }
            String keys[] = functions.get(i).getKeywords().split(",");
            for (int j = 0; j < keys.length; j++) {
                if (keys[j].equals(keywords)) {
                    return functions.get(i);// 找到就返回
                }
            }
        }
        // 运行到这里都没有找到，说明只有类似的关键字，没有同样的
        Function function = new Function();// id 为-1，说明不可用
        function.setKeywords(keywords);
        function.setDescription(similarKeys);
        return function;
    }

    public Function getById(long id) {
        return functionDao.queryById(id);
    }

    /**
     * 通过功能的id集合字符串，获取他们对应的查询结果
     *
     * @param idStr
     * @return
     */
    public String getFunctionResultByIds(String idStr) {
        String results = "";
        String ids[] = idStr.split(",");
        for (int i = 0; i < ids.length; i++) {
            Function function = functionDao.queryById(Long.parseLong(ids[i]));
            results = results + getFunctionResult(function) + ";\n";
        }
        return results;
    }

    /**
     * 通过function的信息和其信息类型来查询function集合
     *
     * @param functionInfos
     * @param type
     * @return
     */
    public ArrayList<Function> getFunctionsByType(String functionInfos, int type) {
        if (functionInfos != null) {
            if (functionInfos.equals("nofunctions")) {
                return null;
            }
        }
        functionInfos = functionInfos.trim().replaceAll("，", ",");
        String infos[] = functionInfos.split(",");
        if (infos.length == 0) {
            return null;
        }
        ArrayList<Function> functions = new ArrayList<Function>();
        for (int i = 0; i < infos.length; i++) {
            Function function = null;
            switch (type) {
                case QUESTIONTYPE_FUNCTION_ID:
                    function = getById(Long.parseLong(infos[i]));
                    break;
                case QUESTIONTYPE_FUNCTION_KEYWORDS:
                    function = getByKeywords(infos[i]);
                    break;
                case QUESTIONTYPE_FUNCTION_NAME:
                    function = getByName(infos[i]);
                    break;
            }
            if (function != null) {
                functions.add(function);
            }
        }
        return functions;
    }

    /**
     * 通过功能的关键字集合字符串，获取他们对应的查询结果
     * 已放弃
     *
     * @param contactHigh
     * @param type
     * @param keysStr
     * @return
     */
    public String getFunctionResultsByKeywords(ContactHigh contactHigh, int type, String keysStr) {
        System.out.println("getFunctionResultsByKeywords : " + keysStr);
        String results = "";
//        String askrecordDes = "";
//        int issuccess = 1;// 当全部查询失败才为0
        int grade = Integer.parseInt(contactHigh.getGrade());
        ArrayList<Function> functions = new ArrayList<Function>();
        if (keysStr != null) {
            if (keysStr.equals("") == false) {
                // 1.先将关键字分组
                keysStr.replaceAll("，", ",");// 将中文逗号替换为英文
                String[] keyStr = keysStr.split(",");
                // 2.根据关键字查询方法
                for (int i = 0; i < keyStr.length; i++) {
                    Function function = null;
                    try {
                        function = getByKeywords(keyStr[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (function != null) {
                        if (function.getId() != -1) {// 找到了这个方法
                            // 判断级别 是否有权限查询
                            if (grade < Integer.parseInt(function.getGrade())) {
                                results = results + "关键字[" + keyStr[i] + "] 由于您未获得与该功能匹配的级别权限，无法进行查询，请与管理员申请后再次尝试查询。\n";
//                                askrecordDes += "查询关键字[" + keyStr[i] + "]对应的功能由于未获得与该功能匹配的级别权限，无法进行查询。";
                            } else {
                                functions = addFunctionsUnique(functions, function);// 去重添加
                            }
                        } else {// 找到了类似关键字的方法
                            results = results + "关键字[" + keyStr[i] + "] 没有找到对应的功能，因此没有获得查询结果。";
//                            askrecordDes += "查询关键字[" + keyStr[i] + "] 没有找到对应的功能，因此没有获得查询结果。";
                            if (function.getDescription() != null) {
                                results = results + "与它类似的关键字有("
                                        + function.getDescription() + ")，请与管理员确认后再次尝试查询。\n";
                            } else {
                                results = results + "请与管理员确认后再次尝试查询。\n";
                            }

                        }
                    } else {// 没找到这个方法
                        results = results + "关键字" + keyStr[i] + " 没有找到对应的功能，因此没有获得查询结果，请与管理员确认后再次尝试查询。\n";
//                        askrecordDes += "查询关键字[" + keyStr[i] + "] 没有找到对应的功能，因此没有获得查询结果。";
                    }
                }
                // 3.根据获得的方法查询其对应的结果
                if (functions.size() != 0) {
                    // 确实查询到了方法
                    results = results + getFunctionResultsByFunctions(functions);
                    for (int i = 0; i < functions.size(); i++) {
//                        askrecordDes += "查询功能[" + functions.get(i).getName() + "] 成功。";
                    }
                } else {
                    results = results + "您所查询的所有关键字均未找到对应的功能，请使用正确的关键字查询，如果你不清楚请联系系统管理员。\n";
//                    askrecordDes += "查询关键字[" + keysStr + "]全部失败。";
//                    issuccess = 0;
                }
            } else {
                results = "请使用正确的关键字查询，如果你不清楚请联系系统管理员。\n";
//                askrecordDes += "查询关键字[" + keysStr + "]全部失败。";
//                issuccess = 0;
            }
        } else {
            results = "请使用正确的关键字查询，如果你不清楚请联系系统管理员。\n";
//            askrecordDes += "查询关键字[" + keysStr + "]全部失败。";
//            issuccess = 0;
        }
//        createAskrecord(contactHigh, type, keysStr, askrecordDes, issuccess);
        return results;
    }

    /**
     * 查询功能集合对应的查询结果
     *
     * @param functions
     * @return
     */
    public String getFunctionResultsByFunctions(ArrayList<Function> functions) {
        String results = "";
        for (int i = 0; i < functions.size(); i++) {
            results = results + getFunctionResult(functions.get(i)) + ";\n";
        }
        return results;
    }


    public int insert(Function function) {
        return functionDao.insert(function);
    }

    public Function queryByName(String name) {
        return functionDao.queryByName(name);
    }

    public ArrayList<Function> queryByKeywords(String keywords) {
        return functionDao.queryByKeywords(keywords);
    }

    public Function queryById(long id) {
        return functionDao.queryById(id);
    }

    public ArrayList<Function> checkKeywords(String keywords) {
        return functionDao.checkKeywords(keywords);
    }

    // 保证每次添加的方法都是唯一的
    private ArrayList<String> addIdsUnique(ArrayList<String> ids, String id) {
        if (ids.size() == 0) {
            ids.add(id);
            return ids;
        }
        boolean isFound = false;
        for (int i = 0; i < ids.size(); i++) {
            if (id.equals(ids.get(i))) {
                isFound = true;
                break;
            }
        }
        if (isFound == false) {
            ids.add(id);
        }
        return ids;
    }

    // 去重添加Functions
    private ArrayList<Function> addFunctionsUnique(ArrayList<Function> functions, Function function) {
        if (functions.size() == 0) {
            functions.add(function);
            return functions;
        }
        boolean isFound = false;
        for (int i = 0; i < functions.size(); i++) {
            if (function.getId() == (functions.get(i).getId())) {
                isFound = true;
                break;
            }
        }
        if (isFound == true) {
            functions.add(function);
        }
        return functions;
    }


    /**
     * 查询是否连接的上指定数据库
     * 如果查询到了就返回该表包含的字段
     *
     * @param function
     * @return
     */
    public ArrayList<String> testConnect(Function function) {
        Connection connection = DBUtil.createConn(function);
        if (connection != null) {
            // 连接上了就读取该表的字段属性
            ArrayList<String> colNames = getTableInfos(connection, function.getTablename());
            DBUtil.close(connection);
            return colNames;
        } else {
            return null;
        }
    }

    /**
     * 获取表字段
     *
     * @param tablename
     */
    private ArrayList<String> getTableInfos(Connection conn, String tablename) {
        String sql = "select * from " + tablename + ";";
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        ArrayList<String> colNames = new ArrayList<String>();
        try {
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                colNames.add(meta.getColumnName(i));//获取字段名
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colNames;
    }

    /**
     * 查询得到该功能的结果（最复杂的方法）
     *
     * @param function
     * @return
     */
    public String getFunctionResult(Function function) {
        String usable = function.getUsable();
        if (usable.equals("no")) {
            return "功能[" + function.getName() + "] 不可用，查询不到结果。";
        }
        String usetype = function.getUsetype();// 使用规则
        String sql = "";
        ArrayList<FieldAndSelfName> readFields;
        if (usetype.equals("sql")) {
            sql = function.getSqlstmt();
            readFields = FunctionFieldUtil.parseFieldSelfName(function.getSqlfields());
        } else {
            readFields = FunctionFieldUtil.parseFieldSelfName(function.getReadfields());// 所需获取的字段
            ArrayList<FunctionFieldRule> fieldRules = FunctionFieldUtil.parseFieldsString(function.getFieldrules());// 判断规则的字段
            // 根据要获取的表和字段名，构造查询语句
            sql = getQueryStatement(function.getTablename(), function.getSortfields(), readFields, fieldRules);
        }
        // 查询并得到结果
        // 1.现根据function获得服务组件
        // 获得数据库链接
        Connection connection = DBUtil.createConn(function);
        // 2.查询
        return "功能[" + function.getName() + "]的查询结果：" + doQuery(connection, sql, readFields);
    }

    /**
     * 通过权限 筛选出可用的功能
     * 筛选出可用功能并告知错误原因
     *
     * @param functions
     * @param grade
     * @return
     */
    public FunctionFilterResult filterFunctions(ArrayList<Function> functions, String grade) {
        if (functions == null || functions.size() == 0) {
            return null;
        }
        ArrayList<Function> fts = new ArrayList<Function>();
        String result = "";
        String functionNames = "";
        for (int i = 0; i < functions.size(); i++) {
            Function function = functions.get(i);
            if (function.getId() == -1) {// 没查询到
                result += "关键字[" + function.getKeywords() + "] 没有查询到对应的功能。";
                if (function.getDescription() != null) {
                    // 有类似的关键字
                    result += "您可能要查询的关键字有[" + function.getDescription() + "]。";
                }
                continue;
            }
            functionNames += function.getName();
            if (i + 1 < functions.size()) {
                functionNames += "、";
            }
            if (Integer.parseInt(function.getGrade()) > Integer.parseInt(grade)) {
                result += "关键字[" + function.getKeywords() + "]对应的功能[" + function.getName() +
                        "]由于你的权限不足，无法查询;\n";
                continue;
            }
            if (function.getUsable().contains("no")) {
                result += "关键字[" + function.getKeywords() + "]对应的的功能[" + function.getName() +
                        "]暂时不可用;\n";
                continue;
            }
            // 权限与功能均可用
            fts.add(function);
        }
        return new FunctionFilterResult(result, functionNames, fts);
    }


    /**
     * 检查Sql语句
     *
     * @param sql
     * @return
     */
    public FunctionSqlResult checkSql(Function function, String sql) {
        FunctionSqlResult functionSqlResult = new FunctionSqlResult();
        // 1.现根据function获得服务组件
        // 获得数据库链接
        Connection conn = DBUtil.createConn(function);
        if (conn != null) {// 连接上了
            if (sql.endsWith(";") == false) {// 可能忘了写分号
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
                    for (int i = 1; i <= meta.getColumnCount(); i++) { // 序号从1开始
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
        } else {
            functionSqlResult.setIsSuccess(-1);
        }
        return functionSqlResult;
    }

    /**
     * 检查关键字
     *
     * @param keywords
     * @return
     */
    public FunctionKeywordsResult checkKeywords(long id, String keywords) {
        FunctionKeywordsResult functionKeywordsResult = new FunctionKeywordsResult();
        if (id > 0) {// 是更新原来的功能，检测其关键字是否更改了
            // 通过id查询原来的关键字
            Function function = functionDao.queryById(id);
            if (keywords.equals(function.getKeywords())) {// 与原来的一样，没有改变
                functionKeywordsResult.setIsSuccess(1);
                return functionKeywordsResult;
            }
        }
        String keys[] = keywords.split(",");// 分割关键字组合
        HashMap<String, String> similarKeys = new HashMap<String, String>();
        for (int i = 0; i < keys.length; i++) {
            ArrayList<Function> rr = functionDao.checkKeywords("%" + keys[i] + "%");// 模糊查询
            if (rr != null) {
                for (int j = 0; j < rr.size(); j++) {
                    String formerKeys[] = rr.get(j).getKeywords().split(",");
                    if (id < 0) {//新建
                        for (String fk : formerKeys) {
                            if (fk.equals(keys[i])) {// 关键字重复
                                functionKeywordsResult.setIsSuccess(-1);//有重复的了
                                similarKeys = putKeyswords(similarKeys, keys[i], fk, true);
                            } else if (fk.contains(keys[i])) {// 关键字有类似的
                                similarKeys = putKeyswords(similarKeys, keys[i], fk, false);
                            }
                        }
                    } else {// 更新
                        long formerId = rr.get(j).getId();
                        for (String fk : formerKeys) {
                            if (fk.equals(keys[i])) {// 关键字重复
                                if (formerId != id) {// 不是原来的关键字
                                    functionKeywordsResult.setIsSuccess(-1);//有重复的了
                                    similarKeys = putKeyswords(similarKeys, keys[i], fk, true);
                                }
                            } else if (fk.contains(keys[i])) {// 关键字有类似的
                                // 更新时发现类似的关键字，不用管
                            }
                        }

                    }
                }
            }
        }

        if (functionKeywordsResult.getIsSuccess() < 0) {// 需要返回错误信息
            ArrayList<String> keysInfo = new ArrayList<String>();
            Iterator iter = similarKeys.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                keysInfo.add((String) entry.getValue());
            }
            functionKeywordsResult.setSimilarKeys(keysInfo);
        }
        return functionKeywordsResult;
    }

    // 将关键字重复信息进行整理
    private HashMap<String, String> putKeyswords(HashMap<String, String> similarKeys, String key, String value, boolean isEqual) {
        String v = "";
        if (similarKeys.containsKey(key)) {// 原来已经添加了这个key的对象
            v = similarKeys.get(key);
        }
        if (isEqual) {// 关键字重复的只会查询到一次 TODO 前台要避免出现 keyA,keyA这样的关键字组合
            v = "关键字" + key + "已存在，类似的还有：" + v;
        } else {
            v = v + "[" + value + "] ";
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
        sql = sql + parseFieldRulesToWhereStmt(fieldRules) + ";";
        return sql;
    }

    /**
     * 将字段规则转换为where子句
     *
     * @param fieldRules
     * @return
     */
    private String parseFieldRulesToWhereStmt(ArrayList<FunctionFieldRule> fieldRules) {
        String whereStmt = " where ";
        if (fieldRules.size() == 0) {
            whereStmt = "";
        } else {
            for (int i = 0; i < fieldRules.size(); i++) {
                // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,EQ#,e,eName,bcde,NE#f,fName,xxx,RG@12BT34
                FunctionFieldRule fieldRule = fieldRules.get(i);

                if (fieldRule.getRule().equals("BB")) {
                    whereStmt = whereStmt + "(" + fieldRule.getField() + " > " + fieldRule.getCompareValue() + ")";
                } else if (fieldRule.getRule().equals("LL")) {
                    whereStmt = whereStmt + "(" + fieldRule.getField() + " < " + fieldRule.getCompareValue() + ")";
                } else if (fieldRule.getRule().equals("EQ")) {
                    if (isNum(fieldRule.getCompareValue())) {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " = " + fieldRule.getCompareValue() + ")";
                    } else {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " = '" + fieldRule.getCompareValue() + "')";
                    }
                } else if (fieldRule.getRule().equals("NE")) {
                    if (isNum(fieldRule.getCompareValue())) {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " != " + fieldRule.getCompareValue() + ")";
                    } else {
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " != '" + fieldRule.getCompareValue() + "')";
                    }
                } else if (fieldRule.getRule().startsWith("RG")) {
                    String rgRule = fieldRule.getRule();
                    String below, above;
                    int indexOfB, indexOfO, indexOfT, indexOfAt;
                    indexOfAt = rgRule.indexOf("@");
                    if (rgRule.contains("BT")) {
                        indexOfB = rgRule.indexOf("B");
                        indexOfT = rgRule.indexOf("T");
                        below = rgRule.substring(indexOfAt + 1, indexOfB);
                        above = rgRule.substring(indexOfT + 1);
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " > " + below + " and " + fieldRule.getField() + " < " + above + ")";
                    } else if (rgRule.contains("OUT")) {
                        indexOfO = rgRule.indexOf("O");
                        indexOfT = rgRule.indexOf("T");
                        below = rgRule.substring(indexOfAt + 1, indexOfO);
                        above = rgRule.substring(indexOfT + 1);
                        whereStmt = whereStmt + "(" + fieldRule.getField() + " < " + below + " or " + fieldRule.getField() + " > " + above + ")";
                    }
                } else if (fieldRule.getRule().equals("NN")) {
                    continue;
                }
                if ((i + 1) < fieldRules.size()) {
                    whereStmt = whereStmt + " and ";
                }
            }
        }
        return whereStmt;
    }

    /**
     * 根据sql语句查询
     *
     * @param conn
     * @param sql
     * @param readFields
     * @return
     */
    private String doQuery(Connection conn, String sql, ArrayList<FieldAndSelfName> readFields) {
        System.out.println("sql: " + sql);
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        String result = "";
        try {
            rs = ps.executeQuery();
            if (rs.next()) {// 只选第一条就行
                for (int i = 0; i < readFields.size(); i++) {
                    FieldAndSelfName fieldAndSelfName = readFields.get(i);
                    result = result + fieldAndSelfName.getSelfName() + ":" + rs.getObject(fieldAndSelfName.getField());
                    if ((i + 1) < readFields.size()) {
                        result = result + ",";
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
                                        if (isReturn) {
                                            value = functionField.getFieldName() + ":" + value + ",大于给定值" + functionField.getCompareValue() + ";";
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
                                        if (isReturn) {
                                            value = functionField.getFieldName() + ":" + value + ",小于给定值" + functionField.getCompareValue() + ";";
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
                                        if (isReturn) {
                                            value = functionField.getFieldName() + ":" + value + ",等于给定值" + functionField.getCompareValue() + ";";
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
                                        if (isReturn) {
                                            value = functionField.getFieldName() + ":" + value + ",不等于给定值" + functionField.getCompareValue() + ";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.startsWith("RG")) {// 范围条件 range
                                        // RG@12BT12 RG@12OUT12 规则示例
                                        // 12BT15:比给定值上浮动15，下浮12;12OUT15:比给定值 下浮超过12 或者 上浮超过15
                                        // 先找出两个范围值
                                        String ts = rule.split("@")[1];
                                        String ranges[] = null;
                                        if (ts.contains("BT")) {
                                            ranges = ts.split("BT");
                                        } else if (ts.contains("OUT")) {
                                            ranges = ts.split("OUT");
                                        }
                                        // 为了简化逻辑，都转化成Float来处理
                                        floatValue = Float.parseFloat(value);
                                        floatCompareValue = Float.parseFloat(functionField.getCompareValue());
                                        // 下限
                                        float downLimit = floatCompareValue - Float.parseFloat(ranges[0]);
                                        // 上限
                                        float upLimit = floatCompareValue + Float.parseFloat(ranges[1]);

                                        if (ts.contains("BT")) {// 中间范围
                                            if (floatValue >= downLimit && floatValue <= upLimit) {// 满足规则
                                                value = functionField.getFieldName() + ":" + value + ",在设定范围内（" + downLimit + "," + upLimit + ");";
                                                isReturns.add(1);
                                                values.add(value);
                                                isReturn = true;
                                            }
                                        } else if (ts.contains("OUT")) {// 两头范围
                                            if (floatValue <= downLimit) {
                                                value = functionField.getFieldName() + ":" + value + ",低于常规范围（-," + downLimit + ");";
                                                isReturns.add(1);
                                                values.add(value);
                                                isReturn = true;
                                            } else if (floatValue >= floatCompareValue) {
                                                value = functionField.getFieldName() + ":" + value + ",高于常规范围（" + upLimit + ",+);";
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
                                            value = functionField.getFieldName() + ":" + value + ",等于给定值" + functionField.getCompareValue() + ";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    } else if (rule.startsWith("NE")) { // 不等于给定值
                                        if (value.contains(functionField.getCompareValue()) == false) {
                                            isReturn = true;
                                            value = functionField.getFieldName() + ":" + value + ",不等于给定值" + functionField.getCompareValue() + ";";
                                            isReturns.add(1);
                                            values.add(value);
                                        }
                                    }
                                }
                                if (isReturn == false) {// 根据规则判断之后 需要返回
                                    value = functionField.getFieldName() + ":" + value + ";";
                                    isReturns.add(0);
                                    values.add(value);
                                }
                            }
                        }
                    }
                    // 判断查询的字段里是否有需要返回的，当一个字段需要返回时，就将所有的都返回了
                    if (isReturns.contains(new Integer(1))) {
                        for (String s : values) {
                            result = result + s;
                        }
                    } else {
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


}
