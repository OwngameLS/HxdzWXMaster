package com.owngame.service;

import com.owngame.entity.ContactHigh;
import com.owngame.entity.Function;
import com.owngame.entity.FunctionKeywordsResult;
import com.owngame.entity.FunctionSqlResult;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-19.
 * 可实现的方法的服务类
 */
public interface FunctionService {
    /**
     * 创建功能
     *
     * @param function
     * @return
     */
    int createFunction(Function function);

    /**
     * 查询所有任务
     */
    ArrayList<Function> queryAll();

    /**
     * 根据权限查询所有的功能
     * @param contactHigh
     * @param type
     * @return
     */
    String queryAllWithGrade(ContactHigh contactHigh, int type);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int deleteById(long id);

    /**
     * 更新
     *
     * @param function
     * @return
     */
    int update(Function function);

    Function getByName(String name);

    Function getByKeywords(String keywords);

    Function getById(long id);

    ArrayList<String> testConnect(Function function);

    /**
     * 通过function得到其对应的结果
     *
     * @param function
     * @return
     */
    String getFunctionResult(Function function);

    /**
     * 检查关键词
     *
     * @param keywords
     * @return
     */
    FunctionKeywordsResult checkKeywords(long id, String keywords);

    /**
     * 检查SQL语句
     *
     * @param function : 主要用于获取数据库连接
     * @param sql
     * @return
     */
    FunctionSqlResult checkSql(Function function, String sql);


    /**
     * 通过功能的id集合字符串，获取他们对应的查询结果
     *
     * @param idStr
     * @return
     */
    String getFunctionResultByIds(String idStr);

    /**
     * 通过关键字集合字符串，获得方法的查询结果
     * @param contactHigh
     * @param type
     * @param keysStr
     * @return
     */
    String getFunctionResultsByKeywords(ContactHigh contactHigh, int type, String keysStr);

    int insert(Function function);

    Function queryByName(String name);

    ArrayList<Function> queryByKeywords(String keywords);

    Function queryById(long id);

    ArrayList<Function> checkKeywords(String keywords);

}
