package com.owngame.service;

import java.util.Map;

/**
 * 初始化定时任务的入口
 * Created by Administrator on 2016-8-30.
 */

public interface HandleQuartzService {

    /**
     * 新增触发器
     * @param strCronExpression
     * @param map
     */
    void addTrigger(String strCronExpression, Map<String, String> map);

    /**
     * 更新触发器
     *
     * @param triggerName
     * @param strCronExpression
     * @param map
     */
    void updateTrigger(String triggerName, String strCronExpression, Map<String, String> map);

    /**
     * 返回触发器的集合
     */
    void showTriggers();

    /**
     * 暂停触发器
     *
     * @param triggerName
     */
    void pauseTrigger(String triggerName);

    /**
     * 恢复触发器
     * @param triggerName
     */
    void resumeTrigger(String triggerName);

    /**
     * 删除触发器
     *
     * @param triggerName
     */
    void deleteTrigger(String triggerName);


}
