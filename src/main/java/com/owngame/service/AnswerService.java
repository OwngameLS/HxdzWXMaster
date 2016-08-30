package com.owngame.service;

import com.owngame.dao.TaskDao;
import com.owngame.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 回答来自手机端的询问
 * Created by Administrator on 2016/8/28.
 */
@Service
public class AnswerService {

    @Autowired
    TaskDao taskDao;

    public Map<String, Object> handleAsk(String actionName) {


        Map<String, Object> map = new HashMap<String, Object>();
        // 有可能是主动查询的 比如 keyword1##13581695827##mobile
        // keyword1代表查询关键词
        // 13581695827为手机号，用于返回
        // mobile 代表是手机端请求逻辑，因为涉及的到微信公众号请求，用于区分

        // 1.先划分actionName确定逻辑走向
        String[] strings = actionName.split("##");
        if (strings[0].equals("netState")) {
            System.out.println("return StateOK");
            // 虽然是询问网络，但是也要去检查下后台有没有任务要返回给手机
            // 检查是否已经有等待的任务了
            ArrayList<Task> tasks = queryTask();
            // 检查是否有定时任务到时间了

            map.put("Type", "StateOK");
        } else {
            System.out.println("return AskResult");

        }
        return map;
    }

    /**
     * 查询需要执行的任务
     * 即网页端确立了某个任务，等待手机端将其读取走
     */
    private ArrayList<Task> queryTask() {
        // 读取数据库 查询是否有新任务
        ArrayList<Task> tasks = taskDao.queryTasksNew();
        return tasks;
    }

    /**
     * 查询定时任务
     */
    private void queryTimeTask() {
        //1. 先检查定时规则
        //2. 根据定时规则生成新任务 TODO 生成新任务的逻辑待编写（通用）
    }
}
