package com.owngame.dao;

import com.owngame.entity.WeixinAccessToken;

/**
 * Created by Administrator on 2016/12/24.
 */
public interface WeixinAccessTokenDao {
    WeixinAccessToken get();

    int insert(WeixinAccessToken weixinAccessToken);

    int update(WeixinAccessToken weixinAccessToken);

    int delete(long id);
}
