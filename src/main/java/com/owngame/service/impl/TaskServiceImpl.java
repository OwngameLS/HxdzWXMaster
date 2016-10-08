package com.owngame.service.impl;

import com.owngame.dao.TaskDao;
import com.owngame.entity.Task;
import com.owngame.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

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
}
