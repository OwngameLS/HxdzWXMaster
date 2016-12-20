package com.owngame.web;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.Task;
import com.owngame.service.AnswerService;
import com.owngame.service.ContactService;
import com.owngame.service.TaskService;
import com.owngame.service.impl.AnswerServiceImpl;
import com.owngame.service.impl.ContactServiceImpl;
import com.owngame.service.impl.FunctionServiceImpl;
import com.owngame.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-7.
 */

@Controller
@RequestMapping("Smserver/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;
    @Autowired
    ContactService contactService;
    @Autowired
    AnswerService answerService;

    /**
     * 从网页创建任务（群发消息）
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> creatTask(@RequestBody Map<String, String> p) {
        String name = p.get("name");
        String description = p.get("description");
        String contents = p.get("contents");
        int sendType = Integer.parseInt(p.get("sendtype"));
        String receivers = p.get("receivers");
        switch (sendType){
            case TaskServiceImpl.SEND_TYPE_SMS:
                taskService.createTask(name, description, contents, receivers);
                break;
            case TaskServiceImpl.SEND_TYPE_WX:
                // TODO 微信任务
//                ArrayList<ContactDisplay> contactDisplays = contactService.1
                break;
            case TaskServiceImpl.SEND_TYPE_SMS_AND_WX:
                taskService.createTask(name, description, contents, receivers);
                // TODO 微信任务
                break;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("success", "success");
        return map;
    }


    /**
     * 查询所有与手机端交互的任务
     *
     * @return
     * @Param lasthours 查询几个小时以内
     */
    @RequestMapping(value = "/{lasthours}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> showTasks(@PathVariable("lasthours") int lasthours) {
        ArrayList<Task> tasks = taskService.queryTasksBeforeTime(lasthours);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tasks", tasks);
        return map;
    }


    /**
     * 处理客户端提交任务处理信息，更新服务器端的数据
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     *
     * @return
     */
    @RequestMapping(value = "/commitTask/{id}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleCommit(@PathVariable("id") long id, @PathVariable("state") int state) {
        return answerService.handleCommit(id, state);
    }


    /**
     * 从手机端发送来询问服务器状态、是否有待处理任务，已经客户端发来的询问信息（想要获取某种信息）
     * 返回json数据格式的方法
     *
     * @return
     */
    @RequestMapping(value = "/askServer/{actionName}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleAsk(@PathVariable("actionName") String actionName) {
        System.out.println("actionName:" + actionName);
        return answerService.handleAskFromPhone(actionName);
    }

    /**
     * 从客户端创建任务（群发消息）
     * @param p
     * @return
     */
    @RequestMapping(value = "/clientqunfa", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> clientqunfa(@RequestBody Map<String, String> p) {
        // qf--sendType==msg==functionIds--groupnames 功能--发送方式==自定义消息内容==功能ids--接收人员组名
        System.out.println("clientqunfa is Called.");
        int sendtype = Integer.parseInt(p.get("sendtype"));
        String msg = p.get("msg");
        String functionIds = p.get("functionIds");
        String groupnames = p.get("groupnames");
        answerService.handleAsk(functionIds,
                FunctionServiceImpl.QUESTIONTYPE_FUNCTION_ID,
                groupnames,
                ContactServiceImpl.CONTACT_TYPE_GROUPS,
                AnswerServiceImpl.ASK_TYPE_CLIENT,
                sendtype,
                msg);
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("type", "StateOK");
        map.put("success", "success");
        return map;
    }
}
