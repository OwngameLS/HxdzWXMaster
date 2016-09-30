package com.owngame.service;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service
public class HandleQuartzServiceImpl implements HandleQuartzService {

    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private Scheduler scheduler;

    private static Date parse(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addTrigger(String strCronExpression, Map<String, String> map) {
        // 添加默认属性的触发器
        schedulerService.schedule(strCronExpression, map);
    }

    public void updateTrigger(String triggerName, String strCronExpression, Map<String, String> map) {



    }

    public void showTriggers() {

    }

    public void pauseTrigger(String triggerName) {
        schedulerService.pauseTrigger(triggerName);
    }

    public void resumeTrigger(String triggerName){
        schedulerService.resumeTrigger(triggerName);
    }

    public void deleteTrigger(String triggerName) {
        schedulerService.removeTrigdger(triggerName);
    }

}
