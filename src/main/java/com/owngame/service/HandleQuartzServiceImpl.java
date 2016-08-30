package com.owngame.service;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service
public class HandleQuartzServiceImpl implements HandleQuartzService {

    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private Scheduler scheduler;

    public void addTrigger() {
        Date startTime1 = parse("2016-08-30 21:56:00");
        Date startTime2 = parse("2016-08-30 21:56:05");
        Date startTime3 = parse("2016-08-30 21:56:10");
        Date startTime4 = parse("2016-08-30 21:56:15");
        Date startTime5 = parse("2016-08-30 21:56:20");
        schedulerService.schedule(startTime1);
        schedulerService.schedule(startTime2);
        schedulerService.schedule(startTime3);
        schedulerService.schedule(startTime4);
        schedulerService.schedule(startTime5);
    }

    public void showTriggers() {
        try {
            ArrayList<String> a = (ArrayList<String>) scheduler.getJobGroupNames();
            for(String s : a){
                System.out.println("aaa:" + s);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

    private static Date parse(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
