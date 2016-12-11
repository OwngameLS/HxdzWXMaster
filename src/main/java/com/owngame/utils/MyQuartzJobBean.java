package com.owngame.utils;

import com.owngame.service.QuartzJobService;
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
        QuartzJobService quartzJobService = getApplicationContext(jobexecutioncontext).getBean("quartzJobService",
                QuartzJobService.class);
        // 因为我设计的就是一个任务一个trigger，所以相应的数据也放在trigger中
        quartzJobService.handleTriggerAsk(triggerName, trigger.getJobDataMap());
    }

    private ApplicationContext getApplicationContext(final JobExecutionContext jobexecutioncontext) {
        try {
            return (ApplicationContext) jobexecutioncontext.getScheduler().getContext().get("applicationContextKey");
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
