package com.owngame.utils;

import com.owngame.service.MainService;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 实现自己的JobDetail
 * 它是自己任务的执行入口 应该会有更上级的东西调用它
 * 通过验证 确实如此
 * Created by Administrator on 2016-8-30.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution// 不允许并发执行
public class MyQuartzJobBean extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        Trigger trigger = jobexecutioncontext.getTrigger();
        String triggerName = trigger.getKey().getName();
        MainService mainService = getApplicationContext(jobexecutioncontext).getBean("mainService",
                MainService.class);
        // 因为我设计的就是一个任务一个trigger，所以相应的数据也放在trigger中
        mainService.handleMethod(triggerName, trigger.getJobDataMap());
    }

    private ApplicationContext getApplicationContext(final JobExecutionContext jobexecutioncontext) {
        try {
//            System.out.println("wori nige nidaodi gaobugao 222222!!!");
            return (ApplicationContext) jobexecutioncontext.getScheduler().getContext().get("applicationContextKey");
        } catch (SchedulerException e) {
// logger.error("jobexecutioncontext.getScheduler().getContext() error!", e);
            throw new RuntimeException(e);
        }
    }
}
