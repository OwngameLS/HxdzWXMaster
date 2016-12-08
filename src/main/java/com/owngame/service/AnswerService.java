package com.owngame.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owngame.entity.ContactHigh;
import com.owngame.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
     * 处理主动询问
     *
     * @param question  询问信息，关键字或者方法名，方法id等
     * @param questionType 查询信息（功能 function）的类型 id 0, name 1, keywords 2
     * @param receiversInfo 查询者信息
     * @param receiversType 查询者信息的类型 sms 0, wx 1, superman 2
     * @param askType 查询者方式 sms 0, wx 1, web 2, triggerjob 3
     * @param description 描述
     */
    String handleAsk(String question, int questionType, String receiversInfo, int receiversType, int askType, String description);
}
