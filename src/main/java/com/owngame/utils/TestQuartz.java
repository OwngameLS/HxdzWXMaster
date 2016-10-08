package com.owngame.utils;

import com.owngame.service.QuartzService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-8-30.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
@ActiveProfiles("production")
public class TestQuartz {
    private static Logger LOGGER = LoggerFactory.getLogger(Test.class);
    private static String JOB_GROUP_NAME = "zydGroup";
    private static String TRIGGER_GROUP_NAME = "zydTriggerGroup";
    private static SchedulerFactory sf = new StdSchedulerFactory();
    @Autowired
    private QuartzService quartzService;

    private static Date parse(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            return format.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



/*
//try {
//LOGGER.info("33333333333");
//Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//LOGGER.info("Scheduler starting up...");
//System.out.print("4444444444");
//scheduler.start();
//} catch (Exception exception) {
//
//System.out.print(exception);
//}
//

quartzService.schedule("0/2 * * ? * * *");
Date startTime = parse("2015-08-05 10:15:00");
Date endTime = parse("2015-08-05 15:15:00");
int repeatCount=10;
long repeatInterval=2;
// String name="b870cf9b-c313-4fd6-87ad-e99de7580290";

// quartzService.schedule(name, startTime, endTime, repeatCount, repeatInterval);


quartzService.schedule("0/10 * * ? * * *");




// 2014-08-19 16:33:00开始执行调度
quartzService.schedule(startTime);

// 2014-08-19 16:33:00开始执行调度，2014-08-22 21:10:00结束执行调试
quartzService.schedule(startTime, endTime);

// 2014-08-19 16:33:00开始执行调度，执行5次结束
quartzService.schedule(startTime, 5);

// 2014-08-19 16:33:00开始执行调度，每隔20秒执行一次，执行5次结束
quartzService.schedule(startTime, 5, 20);


*/

// quartzService.schedule("quartz1", "ZYD", "0/2 * * ? * * *");

    @Test
    public void test() throws SchedulerException {//,Exception,ParseException
//schedule ("", Date startTime, Date endTime,int repeatCount, long repeatInterval, String group)
        Date startTime = parse("2016-08-30 20:55:00");
        Date endTime = parse("2016-08-30 20:40:00");
// quartzService.resumeTrigger("f64eee37-62f4-473f-b5b9-d7a76d86c443");

//        quartzService.schedule("0 28 17 * * ? *");
//        quartzService.schedule("f64eee37-62f4-473f-b5b9-d7a76d86c443", "0 0/1 * * * ? *");
//        quartzService.pauseTrigger("f64eee37-62f4-473f-b5b9-d7a76d86c443");
//        quartzService.resumeTrigger("0fdb7134-9701-494c-9879-bbf2e30120b8");
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("functions", "gogogogo");
        map.put("", "13581695827;13974348591;15874358616");
//        quartzService.schedule("0 0/1 * * * ? *", map);
//        quartzService.schedule(startTime);
        quartzService.pauseTrigger("0c7b9fcc-3b3d-4a4d-b3ce-b4892035881b");
// quartzService.schedule(startTime, endTime,5,5);
// quartzService.schedule("ZYD", startTime, endTime, 1, 10);
// quartzService.schedule(startTime);
//        quartzService.removeTrigdger("8b037a34-2c8f-40a9-a3cb-fb34b1dda222");
        int repeatCount = 10;
        long repeatInterval = 2;
// String name="b870cf9b-c313-4fd6-87ad-e99de7580290";

// quartzService.schedule(name, startTime, endTime, repeatCount, repeatInterval);


/* quartzService.schedule("0/10 * * ? * * *");


// 2014-08-19 16:33:00开始执行调度
quartzService.schedule(startTime);

// 2014-08-19 16:33:00开始执行调度，2014-08-22 21:10:00结束执行调试
quartzService.schedule(startTime, endTime);

// 2014-08-19 16:33:00开始执行调度，执行5次结束
quartzService.schedule(startTime, 5);*/

// 2014-08-19 16:33:00开始执行调度，每隔20秒执行一次，执行5次结束
// quartzService.schedule(startTime, 5, 20);

    }
}
