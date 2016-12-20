package com.owngame.service;

import java.util.Map;

/**
 * Created by Administrator on 2016-11-22.
 * 用于处理微信消息的服务接口
 */
public interface WeiXinMessageService {
    String handleMessage(Map<String, String> mapMessage);
    void sendTextMessage(String message, String openIds);
}
