package com.owngame.service.impl;

import com.owngame.dao.WeixinAccessTokenDao;
import com.owngame.entity.WeixinAccessToken;
import com.owngame.service.WeixinAccessTokenService;
import com.owngame.utils.TimeUtil;
import com.owngame.utils.WeixinUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.bean.token.Token;

import java.io.IOException;

/**
 * Created by Administrator on 2016/12/24.
 */
@Service
public class WeixinAccessTokenServiceImpl implements WeixinAccessTokenService{

    @Autowired
    WeixinAccessTokenDao weixinAccessTokenDao;

    boolean isTokenExist = false;

    public WeixinAccessToken get() {
        WeixinAccessToken weixinAccessToken = weixinAccessTokenDao.get();
        // 判断
        if(weixinAccessToken == null){// 数据库中不存在
            isTokenExist = false;
            weixinAccessToken = getTokenFromWeixin();
        }else{
            isTokenExist = true;
            boolean isExpired = TimeUtil.isExpired(weixinAccessToken.getExpiresin());
            if(isExpired){// 存在且过期了，重新获取
                weixinAccessToken = getTokenFromWeixin();
            }
        }
        return weixinAccessToken;
    }

    public int insert(WeixinAccessToken weixinAccessToken) {
        return weixinAccessTokenDao.insert(weixinAccessToken);
    }

    public int update(WeixinAccessToken weixinAccessToken) {
        return weixinAccessTokenDao.update(weixinAccessToken);
    }

    public int delete(long id) {
        return weixinAccessTokenDao.delete(id);
    }


    // 从微信服务器获得Token信息
    // 获得后保存
    private WeixinAccessToken getTokenFromWeixin() {
        Token accessToken = WeixinUtil.getACCESSTOKEN();
        if (accessToken != null) {
            String token = accessToken.getAccess_token();
            String expireTime = TimeUtil.setExpireTime(accessToken.getExpires_in(), 120);
            // 更新数据库
            WeixinAccessToken weixinAccessToken = new WeixinAccessToken();
            weixinAccessToken.setId(1);
            weixinAccessToken.setAccesstoken(token);
            weixinAccessToken.setExpiresin(expireTime);
            int r = 0;
            if(isTokenExist == false){
                r = insert(weixinAccessToken);
            }else{
                r = update(weixinAccessToken);
            }
            if(r > 0){
                return weixinAccessToken;
            }
        }else {
            return null;// 获取失败
        }
        return null;
    }


}
