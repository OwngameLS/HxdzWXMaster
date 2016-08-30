package com.owngame.service;


import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * SimpleService主要执行定时调度业务
 * Created by Administrator on 2016-8-30.
 */
@Service("simpleService")
public class SimpleService implements Serializable {
    public void testMethod(String triggerName) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
        System.out.println(DateUtils.formatDate(new Date()));
    }

    public void testMethod2(String triggerName) {
// 这里执行定时调度业务
        System.out.println("2动态执行了");
        System.out.println(DateUtils.formatDate(new Date()));
    }
}
