package com.owngame.service;

import com.owngame.entity.Keyword;
import com.owngame.entity.Pager;
import com.owngame.entity.Quickanswer;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/28.
 */
public interface QuickanswerService {
    ArrayList<Quickanswer> queryAll();

    int countAll();

    /**
     * 查询所有关键字
     * TODO 需要分页
     *
     * @return
     */
    Pager<Quickanswer> queryAllLimit(int pageSize, int targetPage);

    Quickanswer getResult(String keyname);

    int insert(Quickanswer quickanswer);

    int update(Quickanswer quickanswer);

    ArrayList<Quickanswer> queryLikeName(String keyname);

    int deleteById(long id);

    String checkDuplicateKeywords(int id, String keyname);

    String getHelp();

    ArrayList<Keyword> queryAllKeywords();

    String getResults(String questions);
}
