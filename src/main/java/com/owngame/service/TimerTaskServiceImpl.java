package com.owngame.service;

import com.owngame.dao.Qrtz_triggersDao;
import com.owngame.dao.TimerTaskDao;
import com.owngame.entity.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-9-19.
 */
public class TimerTaskServiceImpl implements TimerTaskService {

    @Autowired
    TimerTaskDao timerTaskDao;
    @Autowired
    Qrtz_triggersDao qrtz_triggersDao;
    @Autowired
    HandleQuartzService handleQuartzService;

    public int createTimerTask(TimerTask timeTask) {
        // 插入之前先获得原有的names
        ArrayList<String> bNames = qrtz_triggersDao.getNames();
        // 插入
        String strCronExpression = timeTask.getFirerules();
        Map<String, String> map = new HashMap<String, String>();
        map.put("functions", timeTask.getFunctions());
        map.put("receivers", timeTask.getReceivers());
        handleQuartzService.addTrigger(strCronExpression, map);
        // 插入之后的names
        ArrayList<String> afNames = qrtz_triggersDao.getNames();
        // 找出插入的name
        String newName;
        for(String s : afNames){
            boolean isFound = false;// 新名字是否找到
            for(String bs : bNames){
                if(s.equals(bs)){
                    continue;// 160919
                }
            }
        }

        // 插入timertask表
        return 0;
    }

    public ArrayList<TimerTask> queryAll() {
        return null;
    }

    public int deleteById(long id) {
        return 0;
    }

    public int update(TimerTask timerTask) {
        return 0;
    }
}
