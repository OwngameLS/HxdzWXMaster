package com.owngame.service;

import com.owngame.utils.AccessTokenUtil;
import com.owngame.utils.InfoFormatUtil;
import org.dom4j.DocumentException;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;

import java.io.IOException;
import java.util.Map;


/**
 * Created by Administrator on 2016-8-18.
 */
public class CoreService extends Thread {

    String s;
    private Map<String, String> map = null;

    public CoreService(String message) {
        this.s = message;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
        // 立即将xml处理成Map
        try {
            map = InfoFormatUtil.xmlToMap(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        String messageJson = MessageHandler.handleMessage(map);
        if (messageJson.equals("notNeed") == false) {// 需要回复
            // 调用客服消息借口回复消息
            String token = AccessTokenUtil.getSavedToken();
            BaseResult br = MessageAPI.messageCustomSend(token, messageJson);
            System.out.println("br:" + br.getErrcode() + "; " + br.getErrmsg());
        }
        System.out.println("执行结束。。。");
    }
}

