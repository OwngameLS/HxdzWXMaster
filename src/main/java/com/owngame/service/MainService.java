package com.owngame.service;


import com.owngame.dao.*;
import com.owngame.entity.Function;
import com.owngame.entity.Task;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * MainService主要执行定时调度业务的逻辑功能指向
 * Created by Administrator on 2016-8-30.
 */
@Service("mainService")
public class MainService implements Serializable {
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private FunctionDao functionDao;
    @Autowired
    FunctionService functionService;

    public void handleMethod(String triggerName, JobDataMap jobDataMap) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
        /* jobDataMap中的信息
            functions:准备进行何种操作 此处是准备何种信息
            receivers:包含了什么信息 此处是准备传递给哪些人
        */
        String functions[] = ((String) jobDataMap.get("functions")).split(",");
        String receiversIds = (String) jobDataMap.get("receivers");
        String contents = "";
        String name = "";
        String description = "";
        // 查询所要结果
        // 根据所涉及的function来处理
        for (int i = 0; i < functions.length; i++) {
            // 拿到function信息
            Function function = functionDao.queryByName(functions[i]);
            name = name + function.getName() + "::";
            description = description + function.getDescription() + "::";
            contents = function.getDescription() + "的结果::";
            contents = contents + functionService.getFunctionResult(function);
        }
        String receivers = "";// 因为上面得到的是ids，这里就查询成对应的手机号码吧
        String receiversArr[] = receiversIds.split(",");
        for (int i = 0; i < receiversArr.length; i++) {
            receivers = receivers + contactDao.queryById(Long.parseLong(receiversArr[i])).getPhone();
            if (i + 1 < receiversArr.length) {
                receivers = receivers + ",";
            }
        }
        // 将结果组织成Task
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setContent(contents);
        task.setReceivers(receivers);
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
