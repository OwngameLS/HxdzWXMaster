package com.owngame.dao;

import com.owngame.entity.Wxzb;

import java.util.List;

/**
 * Created by Administrator on 2016-8-19.
 */
public interface WxzbDao {
    /**
     * 查询最新的记录
     * @return
     */
    Wxzb querylatest();

    /**
     * 查询今天的所有的记录
     * @return
     */
    List<Wxzb> queryToday();
}
