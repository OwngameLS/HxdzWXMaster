package com.owngame.service;

import com.owngame.entity.WeixinAccessToken;

/**
 * Created by Administrator on 2016/12/24.
 */
public interface WeixinAccessTokenService {
    WeixinAccessToken get();
    int insert(WeixinAccessToken weixinAccessToken);
    int update(WeixinAccessToken weixinAccessToken);
    int delete(long id);

}
