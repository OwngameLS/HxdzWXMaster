package com.owngame.service.impl;

import com.owngame.dao.Qrtz_triggersDao;
import com.owngame.dao.TimerTaskDao;
import com.owngame.entity.TimerTask;
import com.owngame.service.QuartzTriggerService;
import com.owngame.service.TimerTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-9-19.
 */
@Service
public class TimerTaskServiceImpl implements TimerTaskService {

    @Autowired
    TimerTaskDao timerTaskDao;
    @Autowired
    Qrtz_triggersDao qrtz_triggersDao;
    @Autowired
    QuartzTriggerService quartzTriggerService;


    public int createTimerTask(TimerTask timerTask) {
        // 插入之前先获得原有的names
        ArrayList<String> bNames = qrtz_triggersDao.getNames();
        // 插入
        String strCronExpression = timerTask.getFirerules();
        Map<String, String> map = new HashMap<String, String>();
        map.put("functions", timerTask.getFunctions());
        map.put("receivers", timerTask.getReceivers());
        map.put("receivetype", timerTask.getReceivetype() + "");
        map.put("description", timerTask.getDescription());
        quartzTriggerService.addTrigger(strCronExpression, map);
        // 插入之后的names
        ArrayList<String> afNames = qrtz_triggersDao.getNames();
        // 找出插入的name
        String newName = null;
        for (String s : afNames) {
            boolean isFound = false;// 新名字是否找到
            for (String bs : bNames) {
                if (s.equals(bs)) {
                    isFound = true;
                    break;// 160919
                }
            }
            if (isFound) {
                continue;
            } else {
                newName = s;
            }
        }
        if (newName != null) {
            timerTask.setName(newName);
            // 还要判断状态 因为可能插入的时候是一个暂停任务
            updateState(newName, timerTask.getState());
            // 插入timertask表
            return timerTaskDao.insert(timerTask);
        } else {
            return -1;
        }
    }

    public ArrayList<TimerTask> queryAll() {
        return timerTaskDao.queryAll();
    }

    public int deleteById(long id) {
        // 先根据id查询触发器 然后删除它
        TimerTask timerTask = timerTaskDao.queryById(id);
        quartzTriggerService.deleteTrigger(timerTask.getName());
        return timerTaskDao.deleteById(id);
    }

    public int update(TimerTask timerTask) {
        // 更新qrtz
        Map<String, String> map = new HashMap<String, String>();
        map.put("functions", timerTask.getFunctions());
        map.put("receivers", timerTask.getReceivers());
        quartzTriggerService.updateTrigger(timerTask.getName(), timerTask.getFirerules(), map);
        String state = timerTask.getState();
        // 更新状态
        updateState(timerTask.getName(), timerTask.getState());
        return timerTaskDao.update(timerTask);
    }

    public TimerTask queryById(long id) {
        return timerTaskDao.queryById(id);
    }

    // 更新状态
    private void updateState(String name, String state) {
        if (state.equals("run")) {
            quartzTriggerService.resumeTrigger(name);
        } else if (state.equals("pause")) {
            quartzTriggerService.pauseTrigger(name);
        }
    }

}
