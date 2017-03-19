package com.owngame.dao;

import com.owngame.entity.Askrecord;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-7.
 */
public interface AskrecordDao {
    ArrayList<Askrecord> queryAll();

    int countRecordsBeforeTime(@Param("time") String time);

    int countAll();

    /**
     * 查询所有任务
     * TODO 需要分页
     *
     * @return
     */
    ArrayList<Askrecord> queryAllLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询所有待处理的任务
     * TODO 需要分页
     *
     * @return
     */
    ArrayList<Askrecord> queryBySuccess(@Param("issuccess") int issuccess);

    /**
     * 查询时间点以前的任务
     *
     * @param time
     * @return
     */
    ArrayList<Askrecord> queryRecordsBeforeTime(@Param("time") String time);

    int insert(Askrecord askrecord);

    int update(Askrecord askrecord);

    Askrecord queryByPhone(String phone);

    Askrecord queryById(long id);

    int deleteById(long id);

}
