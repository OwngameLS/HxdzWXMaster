package com.owngame.service;

import com.owngame.entity.Task;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/7.
 */
public interface TaskService {
    ArrayList<Task> queryTasksBeforeTime(int hours);
}
