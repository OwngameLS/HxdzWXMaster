package com.owngame.service;

import com.owngame.menu.ManageMenu;

/**
 * 查询各项数据的接口
 * Created by Administrator on 2016-8-19.
 */
public class QueryService {

    public static String handleQuery(String eventKey) {
        String queryResult = "等待完善中，不要急！";
        if (ManageMenu.EVENTKEY_FDJZ.equals(eventKey)) {
            queryResult += "发电机组情况如下：";
        } else if (ManageMenu.EVENTKEY_WXZB.equals(eventKey)) {
            queryResult += "五项指标情况如下：";
        } else if (ManageMenu.EVENTKEY_JRGK.equals(eventKey)) {
            queryResult += "今日概况如下：";
        }
        return queryResult;
    }
}
