package com.owngame.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.owngame.dao.WeixinAccessTokenDao;
import com.owngame.entity.MYUser;
import com.owngame.entity.Settings;
import com.owngame.entity.WeixinAccessToken;
import com.owngame.service.SettingsService;
import com.owngame.service.WeixinMessageService;
import com.owngame.service.WeixinService;
import com.owngame.utils.InfoFormatUtil;
import com.owngame.utils.NetUtil;
import com.owngame.utils.TimeUtil;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/24.
 */
@Service
public class WeixinServiceImpl implements WeixinService {

    // Owngame4JS公众微信号的相关信息
    // private static final String APPID = "wxca12fb4f568feff2";
    // private static final String APPSECRET =
    // "83e81efff357e8f31843695a3c7746fd";
    // 测试账号
//    public static final String APPID = "wxac0bb909a8c87ebc";
//    public static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";

    // 自定义菜单相关功能API请求接口连接
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    private static final String QUERY_USERINFOBYOPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    @Autowired
    WeixinAccessTokenDao weixinAccessTokenDao;
    @Autowired
    SettingsService settingsService;
    @Autowired
    WeixinMessageService weixinMessageService;

    boolean isTokenExist = false;

    /**
     * 获得自身存储的AccessToken（不存在则从微信服务器获取）
     *
     * @return
     */
    public WeixinAccessToken getAccessToken() {
        WeixinAccessToken weixinAccessToken = weixinAccessTokenDao.get();
        // 判断
        if (weixinAccessToken == null) {// 数据库中不存在
            isTokenExist = false;
            weixinAccessToken = getTokenFromWeixin();
        } else {
            isTokenExist = true;
            // 判断内容不为空
            System.out.println("token:" + weixinAccessToken.getAccesstoken());
            boolean isExpired = false;
            if(null == weixinAccessToken.getAccesstoken() || "".equals(weixinAccessToken.getAccesstoken())){
                isExpired = true;
            }else if(null == weixinAccessToken.getExpiresin() || "".equals(weixinAccessToken.getExpiresin())){
                isExpired = true;
            }else{
                isExpired = TimeUtil.isExpired(weixinAccessToken.getExpiresin());
            }
            if (isExpired) {// 存在且过期了，重新获取
                weixinAccessToken = getTokenFromWeixin();
            }
        }
        return weixinAccessToken;
    }

    /**
     * 从头处理最原始的Message
     *
     * @param rawMessage
     */
    public void handleRawMessage(String rawMessage) {
        System.out.println("处理消息中......");
        // 立即将xml处理成Map
        Map<String, String> map = null;
        try {
            map = InfoFormatUtil.xmlToMap(rawMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        String messageJson = null;
        Settings settings = settingsService.queryByName("wx_hasmp");
        if (settings.getValue().equals("false")) {
            messageJson = weixinMessageService.initTextMessageOfJsonString(map.get("FromUserName").toString(), "管理员停用了微信功能。");
        } else {
            messageJson = weixinMessageService.handleMessage(map);
        }
        if (messageJson.equals("notNeed") == false) {// 需要回复
            // 调用客服消息借口回复消息
            sendTextMessageByCustomAPI(messageJson);
        }
        System.out.println("执行结束.");
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


    /**
     * 获得微信用户的微信信息
     *
     * @param openId
     * @return
     */
    public MYUser getUserInfos(String openId) {
        String token = getAccessToken().getAccesstoken();
        User u = UserAPI.userInfo(token, openId);
        MYUser mu = new MYUser(u);
        return mu;
    }

    private BaseResult sendTextMessageByCustomAPI(String messageJson) {
        String token = getAccessToken().getAccesstoken();
        BaseResult br = MessageAPI.messageCustomSend(token, messageJson);
        System.out.println("br:" + br.getErrcode() + "; " + br.getErrmsg());
        return br;
    }

    // 从微信服务器获得Token信息
    // 获得后保存
    private WeixinAccessToken getTokenFromWeixin() {
        Token accessToken = getACCESSTOKEN();
        if (accessToken != null) {
            String token = accessToken.getAccess_token();
            String expireTime = TimeUtil.setExpireTime(accessToken.getExpires_in(), 120);
            // 更新数据库
            WeixinAccessToken weixinAccessToken = new WeixinAccessToken();
            weixinAccessToken.setId(1);
            weixinAccessToken.setAccesstoken(token);
            weixinAccessToken.setExpiresin(expireTime);
            int r = 0;
            if (isTokenExist == false) {
                r = insert(weixinAccessToken);
            } else {
                r = update(weixinAccessToken);
            }
            if (r > 0) {
                return weixinAccessToken;
            }
        } else {
            return null;// 获取失败
        }
        return null;
    }

    /**
     * 获取accessToken的方法
     *
     * @return
     */
    private Token getACCESSTOKEN() {
        Settings appId = settingsService.queryByName("wx_appid");
        Settings appSecret = settingsService.queryByName("wx_appsecret");
        return TokenAPI.token(appId.getValue(), appSecret.getValue());
    }

    /**
     * 菜单的创建 将设置好的菜单信息提交给服务器，微信就会自己处理了 以后这个方法用于当公众微信号的管理者自己需要更新菜单时，调用
     *
     * @return
     */
    public int createMenu(String token, String menu) {
        int result = 0;
        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = null;
        try {
            jsonObject = NetUtil.doPostStr(url, menu);
            if (jsonObject != null) {
                result = jsonObject.getIntValue("errcode");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 删除菜单
    private int deleteMenu(String token) {
        int result = 0;
        String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
        JSONObject jsonObject = null;
        try {
            jsonObject = NetUtil.doGetStr(url);
            if (jsonObject != null) {
                result = jsonObject.getIntValue("errcode");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
