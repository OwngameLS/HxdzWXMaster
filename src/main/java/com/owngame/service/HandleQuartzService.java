package com.owngame.service;

import java.util.Map;

/**
 * 初始化定时任务的入口
 * Created by Administrator on 2016-8-30.
 */

public interface HandleQuartzService {

    /**
     * 新增触发器
     *
     * @param strCronExpression
     * @param map
     */
    void addTrigger(String strCronExpression, Map<String, String> map);

    /**
     * 更新触发器
     *
     * @param triggerKey
     * @param strCronExpression
     * @param map
     */
    void updateTrigger(String triggerKey, String strCronExpression, Map<String, String> map);

    /**
     * 返回触发器的集合
     */
    void showTriggers();

    /**
     * 暂停触发器
     *
     * @param triggerKey
     */
    void pauseTrigger(String triggerKey);

    /**
     * 删除触发器
     *
     * @param triggerKey
     */
    void deleteTrigger(String triggerKey);


}
