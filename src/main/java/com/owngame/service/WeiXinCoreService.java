package com.owngame.service;

import com.owngame.utils.AccessTokenUtil;
import com.owngame.utils.InfoFormatUtil;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Administrator on 2016-8-18.
 * 微信公众号逻辑路由
 * 161121 将原来的Thread 改为Service 以适应SpringMVC
 */
public interface WeiXinCoreService {
    public void handleMessage(String message);
}

