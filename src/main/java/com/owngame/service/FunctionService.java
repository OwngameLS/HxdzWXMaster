package com.owngame.service;

import com.owngame.entity.Function;

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

    /**
     * 通过function得到其对应的结果
     * @param function
     * @return
     */
    String getFunctionResult(Function function);

}
