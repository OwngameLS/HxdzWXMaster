package com.owngame.dao;

import com.owngame.entity.Task;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/28.
 */
public interface TaskDao {
    Task queryById(long id);

    /**
     * 查询所有待处理的任务
     * TODO 需要分页
     *
     * @return
     */
    ArrayList<Task> queryByState(int state);

    /**
     * 查询所有任务
     * TODO 需要分页
     * 根据偏移量查询秒杀商品列表
     *
     * @return
     */
    ArrayList<Task> queryAllTasks(@Param("offset") int offet, @Param("limit") int limit);

    /**
     * 查询时间点以前的任务
     *
     * @param time
     * @return
     */
    ArrayList<Task> queryTasksBeforeTime(String time);

    /**
     * 插入新任务
     *
     * @param task
     * @return
     */
    int insert(Task task);

    int delete(long id);

    int update(Task task);

    int updateState(Task task);
}
