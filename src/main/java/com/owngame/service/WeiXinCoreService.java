package com.owngame.service;

/**
 * Created by Administrator on 2016-8-18.
 * 微信公众号逻辑路由
 * 161121 将原来的Thread 改为Service 以适应SpringMVC
 */
public interface WeiXinCoreService {
    /**
     * 处理消息
     *
     * @param message
     */
    void handleMessage(String message);
}

