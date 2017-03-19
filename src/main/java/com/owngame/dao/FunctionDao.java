package com.owngame.dao;

import com.owngame.entity.Function;
import com.owngame.entity.Keyword;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-27.
 */
public interface FunctionDao {
    ArrayList<Function> queryAll();

    int countAll();

    ArrayList<Function> queryWithLimit(@Param("offset") int offset, @Param("limit") int limit);

    ArrayList<Function> queryAllUsable();

    int insert(Function function);

    int update(Function function);

    Function queryByName(String name);

    ArrayList<Function> queryByKeywords(String keywords);

    ArrayList<Keyword> queryAllKeywords();

    Function queryById(long id);

    int deleteById(long id);

    ArrayList<Function> checkKeywords(String keywords);
}
