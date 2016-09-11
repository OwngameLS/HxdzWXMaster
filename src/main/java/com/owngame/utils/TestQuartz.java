package com.owngame.utils;

import com.owngame.service.SchedulerService;
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
    private SchedulerService schedulerService;

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

schedulerService.schedule("0/2 * * ? * * *");
Date startTime = parse("2015-08-05 10:15:00");
Date endTime = parse("2015-08-05 15:15:00");
int repeatCount=10;
long repeatInterval=2;
// String name="b870cf9b-c313-4fd6-87ad-e99de7580290";

// schedulerService.schedule(name, startTime, endTime, repeatCount, repeatInterval);


schedulerService.schedule("0/10 * * ? * * *");




// 2014-08-19 16:33:00开始执行调度
schedulerService.schedule(startTime);

// 2014-08-19 16:33:00开始执行调度，2014-08-22 21:10:00结束执行调试
schedulerService.schedule(startTime, endTime);

// 2014-08-19 16:33:00开始执行调度，执行5次结束
schedulerService.schedule(startTime, 5);

// 2014-08-19 16:33:00开始执行调度，每隔20秒执行一次，执行5次结束
schedulerService.schedule(startTime, 5, 20);


*/

// schedulerService.schedule("quartz1", "ZYD", "0/2 * * ? * * *");

    @Test
    public void test() throws SchedulerException {//,Exception,ParseException
//schedule ("", Date startTime, Date endTime,int repeatCount, long repeatInterval, String group)
        Date startTime = parse("2016-08-30 20:55:00");
        Date endTime = parse("2016-08-30 20:40:00");
// schedulerService.resumeTrigger("f64eee37-62f4-473f-b5b9-d7a76d86c443");

//        schedulerService.schedule("0 28 17 * * ? *");
//        schedulerService.schedule("f64eee37-62f4-473f-b5b9-d7a76d86c443", "0 0/1 * * * ? *");
//        schedulerService.pauseTrigger("f64eee37-62f4-473f-b5b9-d7a76d86c443");
//        schedulerService.resumeTrigger("0fdb7134-9701-494c-9879-bbf2e30120b8");
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("function", "gogogogo");
        map.put("data", "13581695827;13974348591;15874358616");
//        schedulerService.schedule("0 0/1 * * * ? *", map);
//        schedulerService.schedule(startTime);
// schedulerService.pauseTrigger("f4a610a7-5b43-4738-949c-150eec96fb62");
// schedulerService.schedule(startTime, endTime,5,5);
// schedulerService.schedule("ZYD", startTime, endTime, 1, 10);
// schedulerService.schedule(startTime);
        schedulerService.removeTrigdger("8b037a34-2c8f-40a9-a3cb-fb34b1dda222");
        int repeatCount = 10;
        long repeatInterval = 2;
// String name="b870cf9b-c313-4fd6-87ad-e99de7580290";

// schedulerService.schedule(name, startTime, endTime, repeatCount, repeatInterval);


/* schedulerService.schedule("0/10 * * ? * * *");


// 2014-08-19 16:33:00开始执行调度
schedulerService.schedule(startTime);

// 2014-08-19 16:33:00开始执行调度，2014-08-22 21:10:00结束执行调试
schedulerService.schedule(startTime, endTime);

// 2014-08-19 16:33:00开始执行调度，执行5次结束
schedulerService.schedule(startTime, 5);*/

// 2014-08-19 16:33:00开始执行调度，每隔20秒执行一次，执行5次结束
// schedulerService.schedule(startTime, 5, 20);

    }
}
