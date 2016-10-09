package com.owngame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owngame.dao.TaskDao;
import com.owngame.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理客户端提交的信息
 * Created by Administrator on 2016/8/28.
 */
@Service
public class AnswerService {

    @Autowired
    TaskDao taskDao;

    /**
     * 处理主动询问
     *
     * @param actionName
     * @return
     */
    public Map<String, Object> handleAsk(String actionName) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 1.先划分actionName确定逻辑走向
        String[] strings = actionName.split("##");
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
            // 有可能是主动查询的 比如 keyword1##13581695827##mobile
            // keyword1代表查询关键词
            // 13581695827为手机号，用于返回
            // mobile 代表是手机端请求逻辑，因为涉及的到微信公众号请求，用于区分
            System.out.println("return AskResult");
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
        System.out.println("handleCommit....");
        Map<String, Object> map = new HashMap<String, Object>();
        if(state == 0){// 客户端发来没找到该任务，说明该任务已经正在处理了
            state = 1;
        }
        taskDao.updateState(id, state);
        map.put("type", "GOON");
        return map;
    }

    /**
     * 查询需要执行的任务
     * 即网页端确立了某个任务，等待手机端将其读取走
     */
    private ArrayList<Task> queryUnhandleTask() {
        // 读取数据库 查询是否有新任务
        return taskDao.queryByState(0);
    }

    /**
     * 查询定时任务
     */

    private void queryTimeTask() {
        //1. 先检查定时规则
        //2. 根据定时规则生成新任务 TODO 生成新任务的逻辑待编写（通用）
    }
}
