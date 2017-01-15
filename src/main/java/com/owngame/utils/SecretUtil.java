package com.owngame.utils;

import org.springframework.util.DigestUtils;

/**
 * 加密工具
 * <p>
 * Created by Administrator on 2016-12-29.
 */
public class SecretUtil {
    public static final String MYSALT = "a#xp0sF%1";// md5盐值字符串,用于混淆MD5

    /**
     * MD5加密
     *
     * @param data
     * @return
     */
    public static String encodeWithMD5(String data) {
        String ts = data + "/" + MYSALT;
        String md5 = DigestUtils.md5DigestAsHex(ts.getBytes());
        return md5;
    }

    public static void main(String[] args) {
//        long time = System.currentTimeMillis();
//        String ts = (time + 1000 * 60 * 60 * 24 * 30l) + "";
//        System.out.println("ts:" + ts);
        System.out.println(SecretUtil.encodeWithMD5("Axposf1"));
    }

}
