package com.owngame.service.impl;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.Function;
import com.owngame.entity.FunctionFields;
import com.owngame.service.FunctionService;
import com.owngame.utils.DBUtil;
import com.owngame.utils.FunctionFieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    // 最复杂的方法
    public String getFunctionResult(Function function) {
        // 1.现根据function获得服务组件
        // 获得数据库链接
        Connection connection = DBUtil.createConn(function);
        ArrayList<FunctionFields> functionFieldses = FunctionFieldUtil.parseFieldsString(function.getFields());
        // 根据要获取的表和字段名，构造查询语句
        String sql = getQueryStatement(function.getTablename(),function.getSortfields(),functionFieldses);
        // 构造查询并得到结果

        return null;
    }

    /**
     * 构造查询语句
     * @param tableName
     * @param sortfields
     * @param functionFieldses
     * @return
     */
    private String getQueryStatement(String tableName, String sortfields, ArrayList<FunctionFields> functionFieldses){
        String sql = "select ";
        for(int i=0;i<functionFieldses.size();i++){
            sql = sql + functionFieldses.get(i).getFieldName();
            if((i+1)<functionFieldses.size()){
                sql = sql + ",";
            }
        }
        sql = sql + " from " + tableName + "order by ";
        String sortF[] = sortfields.split(",");
        for(int i=0;i<sortF.length;i++){
            sql = sql + sortF[i];
            if((i+1)<sortF.length){
                sql = sql + ",";
            }
        }
        sql = sql + ";";
        return sql;
    }

    // 根据用户的手机号查询其姓名
    private String doQuery(Connection conn, String sql, ArrayList<FunctionFields> functionFieldses) {
		System.out.println("sql: " + sql);
        PreparedStatement ps = DBUtil.prepare(conn, sql);
        ResultSet rs = null;
        String result = null;
        try {
            rs = ps.executeQuery();
            if (rs.next()) {// 只选第一条就行
                // 根据规则来处理查询结果

            }else{
                result = "NONE";
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
