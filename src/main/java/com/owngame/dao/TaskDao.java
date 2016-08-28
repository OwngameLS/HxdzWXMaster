package com.owngame.dao;

import com.owngame.entity.Task;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/28.
 */
public interface TaskDao {
    /**
     * 查询所有待处理的任务
     * @return
     */
    ArrayList<Task> queryTasksNew();

    /**
     * 查询所有任务
     * TODO 需要分页
     * @return
     */
    ArrayList<Task> queryTasks();

    /**
     * 插入新任务
     * @param task
     * @return
     */
    int insert(Task task);

}
