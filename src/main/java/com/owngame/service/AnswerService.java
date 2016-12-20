package com.owngame.service;

import java.util.Map;

/**
 * 处理客户提交的信息
 * Created by Administrator on 2016/8/28.
 */

public interface AnswerService {
    /**
     * 处理从手机发来的主动询问
     *
     * @param actionName
     * @return
     */
    Map<String, Object> handleAskFromPhone(String actionName);

    /**
     * 处理提交信息
     *
     * @param id
     * @param state
     * @return
     */
    Map<String, Object> handleCommit(long id, int state);

    /**
     * 处理询问的逻辑 中心逻辑！！
     * @param question      询问信息，关键字或者方法名，方法id等
     * @param questionType  查询信息（功能 function）的类型 ids 0, names 1, keywords 2
     * @param receiversInfo 查询者信息
     * @param receiversType 查询者信息的类型 phone 0, openid 1, groups 2, superman 3
     * @param askType       查询者方式 sms 0, wx 1, web 2, client 3 triggerjob 4
     * @param sendType      接收方式 sms 0, wx 1, sms_and_wx 3
     * @param description   描述 （可能还含有其他信息）
     */
    String handleAsk(String question, int questionType, String receiversInfo, int receiversType, int askType, int sendType, String description);

    String unknownContact(int contactType);
}
