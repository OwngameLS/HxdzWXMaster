package com.owngame.service;

import com.owngame.entity.TimerTask;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-19.
 */
public interface TimerTaskService {
    /**
     * 创建任务
     *
     * @param timeTask
     * @return
     */
    int createTimerTask(TimerTask timeTask);

    /**
     * 查询所有任务
     */
    ArrayList<TimerTask> queryAll();

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
     * @param timerTask
     * @return
     */
    int update(TimerTask timerTask);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    TimerTask queryById(long id);


}
