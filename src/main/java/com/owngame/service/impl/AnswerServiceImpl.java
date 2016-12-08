package com.owngame.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owngame.entity.ContactDisplay;
import com.owngame.entity.ContactHigh;
import com.owngame.entity.Function;
import com.owngame.entity.Task;
import com.owngame.service.AnswerService;
import com.owngame.service.ContactService;
import com.owngame.service.FunctionService;
import com.owngame.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-8.
 */
@Service
public class AnswerServiceImpl implements AnswerService{

    @Autowired
    TaskService taskService;
    @Autowired
    FunctionService functionService;
    @Autowired
    ContactService contactService;

    /**
     * 处理从手机发来的主动询问
     *
     * @param actionName
     * @return
     */
    public Map<String, Object> handleAskFromPhone(String actionName) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 1.先划分actionName确定逻辑走向
        String[] strings = actionName.split("--");
        if (strings[0].equals("netState")) {
            System.out.println("ask netState");
            // 虽然是询问网络，但是也要去检查下后台有没有任务要返回给手机
            // 检查是否已经有等待处理的任务了
            ArrayList<Task> tasks = queryUnhandleTask();
            if (tasks.size() == 0) {
                map.put("type", "StateOK");
            } else {
                map.put("type", "tasks");
                ObjectMapper mapper = new ObjectMapper();
                // Convert object to JSON string
                String json = "";
                try {
                    json = mapper.writeValueAsString(tasks);
                    System.out.println("json:" + json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                map.put("tasks", json);
            }
        } else {
            // 有可能是主动查询的 比如 keyword1--13581695827--sms
            // keyword1代表查询关键词
            // 13581695827为手机号，用于返回
            handleAsk(strings[0], FunctionServiceImpl.QUESTIONTYPE_FUNCTION_KEYWORDS, strings[1], RECEIVERS_TYPE_SMS, 0);
            map.put("type", "StateOK");
        }
        return map;
    }

    /**
     * 处理提交信息
     *
     * @param id
     * @param state
     * @return
     */
    public Map<String, Object> handleCommit(long id, int state) {
        System.out.println("id:" + id + ",state:" + state);
        Map<String, Object> map = new HashMap<String, Object>();
        if (state == 0) {// 客户端发来没找到该任务，说明该任务已经正在处理了
            state = 1;
        } else if (state == -2) {// 网页发过来重新发送一次 的请求
            state = 0;
        }
        Task task = new Task();
        task.setId(id);
        task.setState(state);
        taskService.updateState(task);
        map.put("type", "GOON");
        return map;
    }

    /**
     * 查询需要执行的任务
     * 即网页端确立了某个任务，等待手机端将其读取走
     */
    private ArrayList<Task> queryUnhandleTask() {
        // 读取数据库 查询是否有新任务
        return taskService.queryByState(0);
    }


    /**
     * 处理主动询问
     *
     * @param question  询问信息，关键字或者方法名，方法id等
     * @param questionType 查询信息（功能 function）的类型 id 0, name 1, keywords 2
     * @param receiversInfo 查询者信息
     * @param receiversType 查询者信息的类型 sms 0, wx 1, superman 2
     * @param askType 查询者方式 sms 0, wx 1, web 2, triggerjob 3
     * @param description 描述
     */
    public static final int ASK_TYPE_SMS = 0,ASK_TYPE_WX = 1,ASK_TYPE_WEB = 2,ASK_TYPE_TRIGGERJOB = 3;

    public String handleAsk(String question, int questionType, String receiversInfo, int receiversType, int askType, String description) {
        // 1.获得查询所需的功能
        ArrayList<Function> functions = functionService.getFunctionsByType(question, questionType);
        // 2.获取用户信息以判断其权限
        ArrayList<ContactDisplay> contactDisplays = contactService.queryDisplayByInfos(receiversInfo, receiversType);
        // 3.查询
        if(receiversType != ContactServiceImpl.CONTACT_TYPE_SUPERMAN){
            // 初步逻辑，不需要查询功能就可以知道的逻辑
        }else{// 管理员查询
            String contents = functionService.getFunctionResultsByFunctions(functions);
            if(askType == ASK_TYPE_WEB){// 通过网页查询 不需要记录
                return contents;
            }
            // 将接受者的手机号提取出来
            String receivers = "";
            for (int i = 0; i < contactDisplays.size(); i++) {
                receivers = receivers + contactDisplays.get(i).getPhone();
                if (i + 1 < contactDisplays.size()) {
                    receivers = receivers + ",";
                }
            }

            String name = "主动查询";
            String description = "用户" + receivers + "主动查询，相关功能为：";
            contents = functionService.getFunctionResultsByKeywords(contactHigh, askType, keywords);
            taskService.createTask(name, description, contents, receivers);
        }



        // 先判断手机号对应的用户是否能获得对应的权限
        ContactHigh contactHigh = contactService.queryHighByPhone(receivers);
        keywords = keywords.trim();
        if(keywords.contains("帮助") || keywords.contains("关键字")){

        }


        return contents;
    }
}
