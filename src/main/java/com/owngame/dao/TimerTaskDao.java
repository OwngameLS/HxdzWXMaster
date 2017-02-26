package com.owngame.dao;

import com.owngame.entity.TimerTask;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-19.
 */
public interface TimerTaskDao {
    int insert(TimerTask timerTask);

    int update(TimerTask timerTask);

    int deleteById(long id);

    TimerTask queryById(long id);

    ArrayList<String> queryAllNames();

    ArrayList<TimerTask> queryAll();

    int countAll();

    ArrayList<TimerTask> queryWithLimit(@Param("offset") int offset, @Param("limit") int limit);

}
