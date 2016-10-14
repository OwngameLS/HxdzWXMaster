package com.owngame.dao;

import com.owngame.entity.Function;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-27.
 */
public interface FunctionDao {
    ArrayList<Function> queryAll();
    int insert(Function function);
    int update(Function function);
    Function queryByName(String name);
    Function queryByKeywords(String keywords);
    Function queryById(long id);
    int deleteById(long id);
    ArrayList<Function> checkKeywords(String keywords);
}
