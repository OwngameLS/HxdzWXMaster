package com.owngame.service;

import com.owngame.dao.MYUser;
import com.owngame.utils.AccessTokenUtil;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.user.User;

/**
 * Created by Administrator on 2016-8-18.
 */
public class UserHandler {

    public static final int IDTYPE_UNION = 0, IDTYPE_OPEN = 1;

    /**
     * 从微信服务端拿到用户信息
     *
     * @param openid
     * @return
     */
    public static MYUser queryUserFromWeixin(String openid) {
        String token = AccessTokenUtil.getSavedToken();
        // 获得用户信息
        User u = UserAPI.userInfo(token, openid);
        MYUser mu = new MYUser(u);
        return mu;

    }

    //TODO
    public static MYUser queryUserById(String fromUserName, int idtypeOpen) {
        return null;
    }

    //TODO
    public static boolean saveUser(MYUser mu) {
        return false;
    }
}
