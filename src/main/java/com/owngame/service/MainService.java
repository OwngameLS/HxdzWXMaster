package com.owngame.service;


import com.owngame.entity.ContactDisplay;
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
    FunctionService functionService;
    @Autowired
    ContactService contactService;
    @Autowired
    TaskService taskService;



    /**
     * 处理定时任务的查询
     *
     * @param triggerName
     * @param jobDataMap
     */
    public void handleTriggerAsk(String triggerName, JobDataMap jobDataMap) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
        /* jobDataMap中的信息
            functions:准备进行何种操作 此处是准备何种信息
            receivers:包含了什么信息 此处是准备传递给哪些人
            description:这个定时任务的描述
        */
        String functions[] = ((String) jobDataMap.get("functions")).split(",");
        String receiversIds = (String) jobDataMap.get("receivers");
        String description = (String) jobDataMap.get("description");
        String contents = "";
        String name = "定时任务(";
        // 查询所要结果
        // 根据所涉及的function来处理
        for (int i = 0; i < functions.length; i++) {
            // 拿到function信息
            Function function = functionService.queryByName(functions[i]);
            name = name + functions[i];
            contents = contents + getFunctionResult(function);
        }
        name = name + ")";
        String receivers = "";// 因为上面得到的是ids，这里就查询成对应的手机号码吧
        String receiversArr[] = receiversIds.split(",");
        for (int i = 0; i < receiversArr.length; i++) {
            receivers = receivers + contactService.queryById(Long.parseLong(receiversArr[i])).getPhone();
            if (i + 1 < receiversArr.length) {
                receivers = receivers + ",";
            }
        }
        System.out.println("handleTriggerAsk contents:" + contents);
        // 将结果组织成Task
        createTask(name, description, contents, receivers);
    }

    /**
     * 处理主动询问
     *
     * @param keywords  关键字
     * @param receivers 查询者
     * @param askType   主动询问的方式（sms 短信, wx 微信）
     */
    public void handleAsk(String keywords, String receivers, String askType) {
        // 先判断手机号对应的用户是否能获得对应的权限
        ContactDisplay contactDisplay = contactService.queryByPhone(receivers);
        String grade = "-1";
        if(contactDisplay != null){
            grade = contactDisplay.getGrade();
        }
        String contents = "";
        String name = "主动查询";
        String description = "用户" + receivers + "主动查询，相关功能为：";
        String keys[] = keywords.split(",");
        contents = functionService.getFunctionResultsByKeywords(grade, keywords);
//        ArrayList<String> functions = new ArrayList<String>();
//        for (int i = 0; i < keys.length; i++) {
//            // 通过关键字查询到对应的功能
//            Function function = functionService.getByKeywords(keys[i]);
//            if (function == null) {
//                contents = contents + "关键字（" + keys[i] + "）错误，没有查询到相关功能。";
//                continue;
//            }
//            if (function.getId() == -1) {
//                contents = contents + "关键字（" + keys[i] + "）错误，您可能要查询的关键字为：" + function.getDescription();
//                continue;
//            }
//            // 查询到了合理的功能，获取功能的结果
//            description = description + function.getName();
//            contents = contents + getFunctionResult(function);
//        }
        System.out.println("handleAsk contents:" + contents);
        if (askType.equals("sms")) {
            createTask(name, description, contents, receivers);
        } else {
            // 微信查询
        }
    }

    /**
     * 整理查询Function 结果
     *
     * @param function
     * @return
     */
    private String getFunctionResult(Function function) {
        String result = "";
        if (function == null) {
            return "该功能不存在";
        }
        if (function.getUsable().equals("no")) {
            return "功能" + function.getName() + "暂时不能使用。";
        }
        result = function.getName() + "(" + function.getDescription() + ")" + "的结果:";
        String temp = functionService.getFunctionResult(function);
        if (temp != null) {
            if (temp.equals("") == false) {
                result = result + temp;
            } else {
                result = result + "未查询到。";
            }
        } else {
            result = result + "未查询到。";
        }
        return result;
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
        taskService.insert(task);
    }
}
