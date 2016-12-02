package com.owngame.utils;

import org.apache.commons.io.FileUtils;
import weixin.popular.bean.token.Token;

import java.io.File;
import java.io.IOException;

// 用来读取和保存AccessToken的工具类
public class AccessTokenUtil {
    // 文件路径
    public static String tokenFilePath = "";
    public static String saperator = "#expireIn#"; // 文件内容分隔符
    public static File tokenFile = null;

    // 从微信服务器获得Token信息
    public static void getTokenFromWeixin() {
        Token accessToken = WeixinUtil.getACCESSTOKEN();
        if (accessToken != null) {
            String token = accessToken.getAccess_token();
            String expireTime = setExpireTime(accessToken.getExpires_in());
            // 写入文件
            if (tokenFile.exists()) {
                try {
                    FileUtils.write(tokenFile, token + saperator + expireTime,
                            "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                boolean r = false;
                try {
                    r = tokenFile.createNewFile();
                    if (r) {
                        try {
                            FileUtils.write(tokenFile, token + saperator
                                    + expireTime, "UTF-8");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // 获得AccessToken后设置过期时间
    private static String setExpireTime(int expiresIn) {
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + (expiresIn - 120) * 1000;// 提前两分钟过期
        return expireTime + "";
    }

    // 判断是否过期
    private static boolean isTokenExpired(String expiredTime) {
        long currentTime = System.currentTimeMillis();
        long expiredTimeLong = Long.valueOf(expiredTime);
        if (expiredTimeLong > currentTime) {// 过期时间大于当前时间，表示没有过期
            return false;
        } else {
            return true;
        }
    }

    // 读取已经得到的并保存到本地的Token
    public static String getSavedToken() {
        // 读取文件内容
        String content = null;
        try {
            content = FileUtils.readFileToString(tokenFile, "UTF-8");
            System.out.println("path:" + tokenFile.getAbsolutePath());
            System.out.println("cur:" + System.currentTimeMillis() + "\n token :" + content);
        } catch (IOException e) {
            getTokenFromWeixin();
            return getSavedToken();
        }
        // 判断决定是否需要请求微信服务器
        if (content != null) {
            if ("".equals(content) == false) {// 有内容
                String[] t = content.split(saperator);
                try {
                    boolean isExpired = isTokenExpired(t[1]);
                    if (isExpired == false) {
                        return t[0];
                    } else {
                        getTokenFromWeixin();
                        return getSavedToken();
                    }
                } catch (Exception e) {// 文件内容不对，说明需要重新获取
                    getTokenFromWeixin();
                    return getSavedToken();
                }
            } else {
                getTokenFromWeixin();
                return getSavedToken();
            }
        } else {
            getTokenFromWeixin();
            return getSavedToken();
        }
    }

    // public static void main(String agrs[]){
    // // AccessTokenUtil ac = new AccessTokenUtil();
    // // ac.getTokenFromWeixin();
    //
    // System.out.println("token:" + tokenFile.getAbsolutePath());
    // }

}
