package com.owngame.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 初始化定时任务的入口
 * Created by Administrator on 2016-8-30.
 */
public class InitTimerUtil implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("程序启动！");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("程序退出！");
    }
}
