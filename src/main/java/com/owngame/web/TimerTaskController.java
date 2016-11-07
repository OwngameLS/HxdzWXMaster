package com.owngame.web;

import com.owngame.entity.TimerTask;
import com.owngame.service.TimerTaskService;
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
@RequestMapping("Smserver/timertask")
public class TimerTaskController {

    @Autowired
    TimerTaskService timerTaskService;

    /**
     * 操作定时任务
     *
     * @param p
     * @param action
     * @return
     */
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleTimerTask(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        // 先判断操作
        if (action.equals("update")) {
            long id = Long.parseLong(p.get("id"));
            TimerTask timerTask;
            if (id <= 0) {// 插入
                timerTask = new TimerTask();
            } else {
                timerTask = timerTaskService.queryById(id);
            }
            timerTask.setFunctions(p.get("functions"));
            timerTask.setReceivers(p.get("contacts"));
            timerTask.setFirerules(p.get("cron"));
            timerTask.setDescription(p.get("description"));
            timerTask.setState(p.get("state"));
            if (id <= 0) {// 插入
                timerTaskService.createTimerTask(timerTask);
            } else {
                timerTaskService.update(timerTask);
            }
        } else if (action.equals("delete")) {
            System.out.println("delete");
            long id = Long.parseLong(p.get("id"));
            timerTaskService.deleteById(id);
        }
        // 返回所有 用于刷新页面
        return queryTimerTasks();
    }

    /**
     * 查询所有定时任务
     *
     * @return
     */
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryTimerTasks() {
        System.out.println("queryTimerTasks...");
        ArrayList<TimerTask> timerTasks = timerTaskService.queryAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timerTasks", timerTasks);
        return map;
    }
}
