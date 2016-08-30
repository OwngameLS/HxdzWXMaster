package com.owngame.utils;

import com.owngame.service.SimpleService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 实现自己的JobDetail
 * Created by Administrator on 2016-8-30.
 */
public class MyQuartzJobBean extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        System.out.println("wori nige nidaodi gaobugao !!!");
        Trigger trigger = jobexecutioncontext.getTrigger();
        String triggerName = trigger.getKey().getName();
        SimpleService simpleService = getApplicationContext(jobexecutioncontext).getBean("simpleService",
                SimpleService.class);
        simpleService.testMethod(triggerName);
    }

    private ApplicationContext getApplicationContext(final JobExecutionContext jobexecutioncontext) {
        try {
            System.out.println("wori nige nidaodi gaobugao 222222!!!");
            return (ApplicationContext) jobexecutioncontext.getScheduler().getContext().get("applicationContextKey");
        } catch (SchedulerException e) {
// logger.error("jobexecutioncontext.getScheduler().getContext() error!", e);
            throw new RuntimeException(e);
        }
    }
}
