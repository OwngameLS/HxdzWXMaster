package com.owngame.service.impl;

import com.owngame.dao.TaskDao;
import com.owngame.entity.Task;
import com.owngame.service.TaskService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/7.
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskDao taskDao;

    /**
     * 查询几个小时之内的任务状态
     *
     * @param hours
     * @return
     */
    public ArrayList<Task> queryTasksBeforeTime(int hours) {
        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.HOUR_OF_DAY, -hours);//取当前时间之前的hours
        //通过格式化输出日期
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(cal.getTime());
        return taskDao.queryTasksBeforeTime(time);
    }

    public void createTask(String name, String description, String contents, String receivers) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setContent(contents);
        task.setReceivers(receivers);
        task.setState(Task.STATE_WAITING);
        task.setCreateTime(new Date(System.currentTimeMillis()));
        // 插入数据库
        insert(task);
    }

    public Task queryById(long id) {
        return taskDao.queryById(id);
    }

    public ArrayList<Task> queryByState(int state) {
        return taskDao.queryByState(state);
    }

    public ArrayList<Task> queryAllTasks(@Param("offset") int offet, @Param("limit") int limit) {
        return taskDao.queryAllTasks(offet, limit);
    }

    public ArrayList<Task> queryTasksBeforeTime(String time) {
        return taskDao.queryTasksBeforeTime(time);
    }

    public int insert(Task task) {
        return taskDao.insert(task);
    }

    public int delete(long id) {
        return taskDao.delete(id);
    }

    public int update(Task task) {
        return taskDao.update(task);
    }

    public int updateState(Task task) {
        return taskDao.updateState(task);
    }
}
