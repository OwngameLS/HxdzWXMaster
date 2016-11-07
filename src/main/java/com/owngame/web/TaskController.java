package com.owngame.web;

import com.owngame.entity.Task;
import com.owngame.service.AnswerService;
import com.owngame.service.MainService;
import com.owngame.service.TaskService;
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
    MainService mainService;

    @Autowired
    TaskService taskService;

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
        String receivers = p.get("receivers");
        mainService.createTask(name, description, contents, receivers);
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
     * <p>
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
     * <p>
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     *
     * @return
     */
    @RequestMapping(value = "/askServer/{actionName}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleAsk(@PathVariable("actionName") String actionName) {
        return answerService.handleAsk(actionName);
    }
}
