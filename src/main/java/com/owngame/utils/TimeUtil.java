package com.owngame.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理与时间有关系的逻辑工具
 * Created by Administrator on 2016-12-29.
 */
public class TimeUtil {


    /**
     * 判断是否过期
      */
    public static boolean isExpired(String expiredTime) {
        long currentTime = System.currentTimeMillis();
        long expiredTimeLong = Long.valueOf(expiredTime);
        if (expiredTimeLong > currentTime) {// 过期时间大于当前时间，表示没有过期
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置过期时间
     * @param expiresIn 过期秒数
     * @param beforeIntTime 提前过期秒数
     * @return
     */
    public static String setExpireTime(int expiresIn, int beforeIntTime) {
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + (expiresIn - beforeIntTime) * 1000;// 提前过期
        return expireTime + "";
    }

    /**
     * 将毫秒类型的时间转换成DateString
     * @param timeMills
     * @return
     */
    public static String parseTimeMillisToDateString(long timeMills){
        Date d = new Date(timeMills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }


    public static void main(String[] agrs){
        long t = System.currentTimeMillis()+60*1000*60*24*365 ;
        System.out.println(parseTimeMillisToDateString(t));
    }



}
