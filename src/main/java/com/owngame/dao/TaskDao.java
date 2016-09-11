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
     * @param offet
     * @param limit
     * @return
     */
    ArrayList<Task> queryTasks(@Param("offset") int offet, @Param("limit") int limit);

    /**
     * 插入新任务
     *
     * @param task
     * @return
     */
    int insert(Task task);

    int delete(long id);

    int update(Task task);

    // TODO 没有实现
    void updateState(long id, int state);
}
