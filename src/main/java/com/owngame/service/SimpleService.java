package com.owngame.service;


import com.owngame.dao.ContactDao;
import com.owngame.dao.FdjzDao;
import com.owngame.dao.TaskDao;
import com.owngame.dao.WxzbDao;
import com.owngame.entity.Task;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

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
    @Autowired
    private ContactDao contactDao;

    public void handleMethod(String triggerName, JobDataMap jobDataMap) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
        /* jobDataMap中的信息
            functions:准备进行何种操作 此处是准备何种信息
            receivers:包含了什么信息 此处是准备传递给哪些人
        */
        String functions = (String) jobDataMap.get("functions");
        String receiversIds = (String) jobDataMap.get("receivers");
        String infomation = "abcdefghijk";
        // 查询所要结果
//        if (functions.equals(func_fdjz)) {
//            infomation = fdjzDao.querylatest().toString();
//        } else if (functions.equals(func_wxzb)) {
//            infomation = wxzbDao.querylatest().toString();
//        }
        String receivers = "";// 因为上面得到的是ids，这里就查询成对应的手机号码吧
        String receiversArr[] = receiversIds.split(",");
        for(int i = 0; i<receiversArr.length;i++){
            receivers = receivers + contactDao.queryById(Long.parseLong(receiversArr[i])).getPhone();
            if(i+1<receiversArr.length){
                receivers = receivers + ",";
            }
        }
        // 将结果组织成Task
        Task task = new Task();
        task.setContent(infomation);
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
