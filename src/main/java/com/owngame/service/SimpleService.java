package com.owngame.service;


import com.owngame.dao.FdjzDao;
import com.owngame.dao.TaskDao;
import com.owngame.dao.WxzbDao;
import com.owngame.entity.Task;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Date;

/**
 * SimpleService主要执行定时调度业务
 * Created by Administrator on 2016-8-30.
 */
@Service("simpleService")
public class SimpleService implements Serializable {
    private String func_wxzb = "wxzb";
    private String func_fdjz = "fdjz";
    @Autowired
    private FdjzDao fdjzDao;
    @Autowired
    private WxzbDao wxzbDao;
    @Autowired
    private TaskDao taskDao;

    public void handleMethod(String triggerName, JobDataMap jobDataMap) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
//        System.out.println(DateUtils.formatDate(new Date()));
        /* jobDataMap中的信息
            function:准备进行何种操作 此处是准备何种信息
            data:包含了什么信息 此处是准备传递给哪些人
        */
        String func = (String) jobDataMap.get("function");
        String data = (String) jobDataMap.get("data");
        String infomation = "";
        // 查询所要结果
        if (func.equals(func_fdjz)) {
            infomation = fdjzDao.querylatest().toString();
        } else if (func.equals(func_wxzb)) {
            infomation = wxzbDao.querylatest().toString();
        }
        // 将结果组织成Task
        Task task = new Task();
        task.setContent(infomation);
        task.setReceivers(data);
        task.setState(Task.STATE_WAITING);
        task.setCreateTime(new Date(System.currentTimeMillis()));
        // 插入数据库
        taskDao.insert(task);
    }

    public void testMethod2(String triggerName) {
// 这里执行定时调度业务
        System.out.println("2动态执行了");
//        System.out.println(DateUtils.formatDate(new Date()));
    }
}
