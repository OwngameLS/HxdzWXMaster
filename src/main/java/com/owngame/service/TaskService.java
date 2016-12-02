package com.owngame.service;

import com.owngame.entity.Task;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/7.
 */
public interface TaskService {
    ArrayList<Task> queryTasksBeforeTime(int hours);

    Task queryById(long id);

    ArrayList<Task> queryByState(int state);

    ArrayList<Task> queryAllTasks(@Param("offset") int offet, @Param("limit") int limit);

    ArrayList<Task> queryTasksBeforeTime(String time);

    int insert(Task task);

    int delete(long id);

    int update(Task task);

    int updateState(Task task);
}
