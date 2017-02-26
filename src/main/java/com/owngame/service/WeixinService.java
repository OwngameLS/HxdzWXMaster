package com.owngame.service;

import com.owngame.entity.MYUser;
import com.owngame.entity.WeixinAccessToken;

/**
 * 微信开发模式下的各类功能的接口
 * Created by Administrator on 2016/12/24.
 */
public interface WeixinService {
    /**
     * 获得微信AccessToken
     *
     * @return
     */
    WeixinAccessToken getAccessToken();

    /**
     * 处理最原始的Message
     *
     * @param rawMessage
     * @return
     */
    void handleRawMessage(String rawMessage);

    MYUser getUserInfos(String openId);

}
