package com.owngame.service.impl;

import com.owngame.service.WeiXinCoreService;
import com.owngame.service.WeiXinMessageService;
import com.owngame.utils.AccessTokenUtil;
import com.owngame.utils.InfoFormatUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-21.
 */

@Service
public class WeiXinCoreServiceImpl implements WeiXinCoreService {
    @Autowired
    WeiXinMessageService weixinMessageService;

    /**
     * 处理消息
     * @param message
     */
    public void handleMessage(String message) {
        System.out.println("处理消息中......");
        // 立即将xml处理成Map
        Map<String, String> map = null;
        try {
            map = InfoFormatUtil.xmlToMap(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        String messageJson = weixinMessageService.handleMessage(map);
        if (messageJson.equals("notNeed") == false) {// 需要回复
            // 调用客服消息借口回复消息
            String token = AccessTokenUtil.getSavedToken();
            BaseResult br = MessageAPI.messageCustomSend(token, messageJson);
            System.out.println("br:" + br.getErrcode() + "; " + br.getErrmsg());
        }
        System.out.println("执行结束.");
    }
}
