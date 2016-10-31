package com.owngame.service;


import com.owngame.dao.*;
import com.owngame.entity.Function;
import com.owngame.entity.Task;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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
        String description = (String) jobDataMap.get("description");// 定时任务的描述
        String contents = "";// 查询得到的结果
        String name = "查询以下功能：";

        // 查询所要结果
        // 根据所涉及的function来处理
        for (int i = 0; i < functions.length; i++) {
            // 拿到function信息
            Function function = functionDao.queryByName(functions[i]);
            if (function.getUsable().equals("yes")) {
                name = name + function.getName() + "；";
                description = description + function.getDescription() + "；";
                contents = contents + getFunctionResult(function) + "；";
            }
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
        createTask(name, description, contents, receivers);
    }

    private void createTask(String name, String description, String contents, String receivers){
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

    private String getFunctionResult(Function function) {
        String result = "";
        if (function.getUsable().equals("yes")) {
            result = function.getName() + "查询结果:";
            String temp = functionService.getFunctionResult(function);
            if (temp != null) {
                if (temp.equals("") == false) {// 确实查询到了内容
                    result = result + temp;
                } else {
                    result = result + "未查询到。";
                }
            } else {
                result = result + "未查询到。";
            }
        } else {
            result = "功能" + function.getName() + "不可用，需要联系管理员解决。";
        }
        return result;
    }

    /**
     * 应答关键词查询
     * @param keywords
     * @param receivers
     */
    public void handleAsk(String keywords, String receivers) {
        // 有可能是主动查询的 比如 keyword1##13581695827##mobile
        // keyword1代表查询关键词
        // 13581695827为手机号，用于返回
        // mobile 代表是手机端请求逻辑，因为涉及的到微信公众号请求，用于区分
//            根据关键字查询功能
        String description = "";// 定时任务的描述
        String contents = "";// 查询得到的结果
        String name = "查询以下功能：";

        String keys[] = keywords.split(",");
        ArrayList<String> functions = new ArrayList<String>();
        for (int i=0; i<keys.length;i++){
            Function function = functionDao.queryByKeywords(keys[i]);
            if(function == null){// 关键字不存在
                contents = contents + "您查询的关键字（" + keys[i] + "）不存在" + ";";
                description = description + "关键字（" + keys[i] + "）查询错误" + ";";
                name = name + "关键字（" + keys[i] + "）查询错误" + ";";
            }else{
                contents = contents + getFunctionResult(function) + ";";
                description = description + function.getDescription() + ";";
                name = name + function.getName() + ";";
            }
        }
        // 将结果组织成Task
        createTask(name, description, contents, receivers);
    }
}
