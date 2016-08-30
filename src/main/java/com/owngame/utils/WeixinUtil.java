package com.owngame.utils;

import com.alibaba.fastjson.JSONObject;
import com.owngame.menu.ManageMenu;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.token.Token;

import java.io.IOException;

/**
 * Created by Administrator on 2016-8-18.
 */
public class WeixinUtil {
    // Owngame4JS公众微信号的相关信息
    // private static final String APPID = "wxca12fb4f568feff2";
    // private static final String APPSECRET =
    // "83e81efff357e8f31843695a3c7746fd";
    // 测试账号
    public static final String APPID = "wxac0bb909a8c87ebc";
    public static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 自定义菜单相关功能API请求接口连接
    private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    private static final String QUERY_USERINFOBYOPENID_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    /**
     * 获取accessToken的方法
     *
     * @return
     */

    public static Token getACCESSTOKEN() {
        return TokenAPI.token(APPID, APPSECRET);
    }

    /**
     * 菜单的创建 将设置好的菜单信息提交给服务器，微信就会自己处理了 以后这个方法用于当公众微信号的管理者自己需要更新菜单时，调用
     *
     * @return
     */
    public static int createMenu(String token, String menu) {
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
    public static int deleteMenu(String token) {
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

    public static void main(String agrs[]) {
        String token = AccessTokenUtil.getSavedToken();

        String jStr = JSONObject.toJSONString(ManageMenu.createMenu());

        int r = WeixinUtil.createMenu(token, jStr);
        System.out.println("jStr:" + jStr + ": r:" + r);


//        System.out.println("r:" + r.getErrcode() + "," + r.getErrmsg() + "," + r.getUrl());


    }
}
