package com.owngame.service;

import com.owngame.entity.Askrecord;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-7.
 */
public interface AskrecordService {

    ArrayList<Askrecord> handleQuery(int lasthours, int type, String askers, String functions, int issuccess);

    ArrayList<Askrecord> queryAll();

    /**
     * 查询所有任务
     * TODO 需要分页
     * 根据偏移量查询秒杀商品列表
     *
     * @return
     */
    ArrayList<Askrecord> queryAllLimit(@Param("offset") int offet, @Param("limit") int limit);

    /**
     * 查询所有待处理的任务
     * TODO 需要分页
     *
     * @return
     */
    ArrayList<Askrecord> queryBySuccess(int issuccess);


    /**
     * 查询时间点以前的任务
     *
     * @param lasthours
     * @return
     */
    ArrayList<Askrecord> queryRecordsBeforeTime(int lasthours);

    int insert(Askrecord askrecord);

    int update(Askrecord askrecord);

    Askrecord queryByPhone(String phone);

    Askrecord queryById(long id);

    int deleteById(long id);
}
