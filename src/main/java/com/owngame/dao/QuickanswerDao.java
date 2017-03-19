package com.owngame.dao;

import com.owngame.entity.Keyword;
import com.owngame.entity.Quickanswer;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-7.
 */
public interface QuickanswerDao {
    ArrayList<Quickanswer> queryAll();

    int countAll();

    /**
     * 查询所有关键字
     * TODO 需要分页
     *
     * @return
     */
    ArrayList<Quickanswer> queryAllLimit(@Param("offset") int offset, @Param("limit") int limit);

    Quickanswer getResult(String keyname);

    Quickanswer queryById(long id);

    int insert(Quickanswer quickanswer);

    int update(Quickanswer quickanswer);

    ArrayList<Quickanswer> queryLikeName(String keyname);

    ArrayList<Keyword> queryAllKeywords();

    int deleteById(long id);

}
