package com.owngame.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owngame.entity.*;
import com.owngame.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-8.
 */
@Service
public class AnswerServiceImpl implements AnswerService {

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
    public static final int ASK_TYPE_SMS = 0, ASK_TYPE_WX = 1, ASK_TYPE_WEB = 2, ASK_TYPE_CLIENT= 3, ASK_TYPE_TRIGGERJOB = 4;
    @Autowired
    TaskService taskService;
    @Autowired
    FunctionService functionService;
    @Autowired
    ContactService contactService;
    @Autowired
    AskrecordService askrecordService;

    /**
     * 处理从手机发来的主动询问
     *
     * @param actionName
     * @return
     */
    public Map<String, Object> handleAskFromPhone(String actionName) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 1.先划分actionName确定逻辑走向
        String[] strings = actionName.split("--");
        if (strings[0].equals("netState")) {
            System.out.println("ask netState");
            // 虽然是询问网络，但是也要去检查下后台有没有任务要返回给手机
            // 检查是否已经有等待处理的任务了
            ArrayList<Task> tasks = queryUnhandleTask();
            if (tasks.size() == 0) {
                map.put("type", "StateOK");
            } else {
                map.put("type", "tasks");
                ObjectMapper mapper = new ObjectMapper();
                // Convert object to JSON string
                String json = "";
                try {
                    json = mapper.writeValueAsString(tasks);
                    System.out.println("json:" + json);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                map.put("tasks", json);
            }
        } else if(strings[0].equals("usableFunctions")){// 查询可用功能
            ArrayList<Function> functions = functionService.queryAllUsable();
            map.put("type", "functions");
            ObjectMapper mapper = new ObjectMapper();
            // Convert object to JSON string
            String json = "";
            try {
                json = mapper.writeValueAsString(functions);
                System.out.println("json functions:" + json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            map.put("functions", json);
            ArrayList<String> groups = contactService.getGroups();
            try {
                json = mapper.writeValueAsString(groups);
                System.out.println("json groups:" + json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            map.put("groups", json);
        } else if(strings[0].equals("qf")||strings[0].equals("群发")){// 管理员通过命令群发
            // qf--functionIds--groupnames 功能和接收人员组名
            handleAsk(strings[1], FunctionServiceImpl.QUESTIONTYPE_FUNCTION_ID, strings[2], ContactServiceImpl.CONTACT_TYPE_GROUPS, ASK_TYPE_CLIENT, "");
            map.put("type", "StateOK");
        } else {
            // 有可能是主动查询的 比如 keyword1--13581695827--sms
            // keyword1代表查询关键词
            // 13581695827为手机号，用于返回
            handleAsk(strings[0], FunctionServiceImpl.QUESTIONTYPE_FUNCTION_KEYWORDS, strings[1], ContactServiceImpl.CONTACT_TYPE_PHONE, ASK_TYPE_SMS, "");
            map.put("type", "StateOK");
        }
        return map;
    }

    /**
     * 处理提交信息
     *
     * @param id
     * @param state
     * @return
     */
    public Map<String, Object> handleCommit(long id, int state) {
        System.out.println("id:" + id + ",state:" + state);
        Map<String, Object> map = new HashMap<String, Object>();
        if (state == 0) {// 客户端发来没找到该任务，说明该任务已经正在处理了
            state = 1;
        } else if (state == -2) {// 网页发过来重新发送一次 的请求
            state = 0;
        }
        Task task = new Task();
        task.setId(id);
        task.setState(state);
        taskService.updateState(task);
        map.put("type", "GOON");
        return map;
    }

    /**
     * 查询需要执行的任务
     * 即网页端确立了某个任务，等待手机端将其读取走
     */
    private ArrayList<Task> queryUnhandleTask() {
        // 读取数据库 查询是否有新任务
        return taskService.queryByState(0);
    }

    public String handleAsk(String question, int questionType, String receiversInfo, int receiversType, int askType, String description) {
        try {
            // 1.获得查询所需的功能
            ArrayList<Function> functions = functionService.getFunctionsByType(question, questionType);
            System.out.println("query function :"+functions.size());
            // 2.获取用户信息以判断其权限
            ArrayList<ContactDisplay> contactDisplays = contactService.queryDisplayByInfos(receiversInfo, receiversType);
            System.out.println("query contacts :"+contactDisplays.size());
            String result = "";
            // 查询记录
            Askrecord askrecord = new Askrecord();
            askrecord.setType(askType);
            // 3.查询
            if (askType == ASK_TYPE_SMS || askType == ASK_TYPE_WX) {// 用户查询
                // 没有查询到用户信息，直接返回
                if (contactDisplays == null || contactDisplays.size() == 0) {
                    return unknownContact(receiversType);
                }
                // 获得查询用户的信息
                ContactDisplay contactDisplay = contactDisplays.get(0);
                ContactHigh contactHigh = contactDisplay.initHighObject();
                askrecord.setName(contactDisplay.getName());
                askrecord.setPhone(contactDisplay.getPhone());
                // 没有查询到对应的功能，直接返回
                if (question.contains("帮助") == false || question.contains("关键字") == false) {// 用户不是请求帮助、不是查询关键字信息
                    if (functions == null || functions.size() == 0) {
                        askrecord.setFunctions(question);
                        askrecord.setDescription("查询的关键字没有对应的功能，此次查询失败。");
                        askrecord.setIssuccess(Askrecord.ASK_RESULT_FAILED);
                        createAskrecord(askrecord);
                        return "您查询的关键字没有对应的功能，此次查询失败。";
                    }
                }
                if (question.contains("帮助")) {
                    askrecord.setFunctions("帮助信息");
                    askrecord.setDescription("查询帮助信息。");
                    askrecord.setIssuccess(Askrecord.ASK_RESULT_SUCCESS);
                    createAskrecord(askrecord);
                    return getHelp(receiversType);
                }
                if (question.contains("关键字")) {// 返回关键字信息
                    askrecord.setFunctions("查询关键字信息");
                    askrecord.setDescription("查询功能对应的关键字信息。");
                    askrecord.setIssuccess(Askrecord.ASK_RESULT_SUCCESS);
                    createAskrecord(askrecord);
                    return functionService.queryAllWithGrade(contactHigh, questionType);
                }
                String name = "用户主动查询";
                description = "用户[" + contactDisplay.getName() + "]主动查询，相关功能为：";
                askrecord.setFunctions(question);
                // 先判断查询信息对应的用户是否能获得对应的权限
                // 权限判断
                ArrayList<Function> functions2 = new ArrayList<Function>();// 满足用户权限、使用要求用于查询的
                for (int i = 0; i < functions.size(); i++) {
                    Function function = functions.get(i);
                    if (function.getId() == -1) {// 没查询到
                        result += "关键字[" + function.getKeywords() + "] 没有查询到对应的功能。";
                        if (function.getDescription() != null) {
                            // 有类似的关键字
                            result += "您可能要查询的关键字有[" + function.getDescription() + "]。";
                        }
                        continue;
                    }
                    description += function.getName();
                    if (i + 1 < functions.size()) {
                        description += "、";
                    }
                    if (Integer.parseInt(function.getGrade()) > Integer.parseInt(contactDisplay.getGrade())) {
                        result += "关键字[" + function.getKeywords() + "]对应的功能由于你的权限不足，无法查询;\n";
                        continue;
                    }
                    if (function.getUsable().contains("no")) {
                        result += "关键字[" + function.getKeywords() + "]所查询的功能暂时不可用;\n";
                        continue;
                    }
                    // 权限与功能均可用
                    functions2.add(function);
                }
                // 查询功能结果
                result += functionService.getFunctionResultsByFunctions(functions2);
                // 记录查询操作
                askrecord.setDescription(result);
                createAskrecord(askrecord);
                // 根据查询来源，返回结果的方式也有不同
                if (receiversType == ContactServiceImpl.CONTACT_TYPE_PHONE) {
                    // 创建任务
                    taskService.createTask(name, description, result, contactDisplay.getPhone());
                } else if (receiversType == ContactServiceImpl.CONTACT_TYPE_OPENID) {
                    return result;
                }
            } else {// 管理员查询
                result = functionService.getFunctionResultsByFunctions(functions);
                String fs = "";
                for (int i = 0; i < functions.size(); i++) {
                    fs += functions.get(i).getKeywords();
                    if (i + 1 < functions.size()) {
                        fs += "、";
                    }
                }
                askrecord.setFunctions(fs);
                if (askType == ASK_TYPE_WEB) {// 通过网页查询
                    askrecord.setName("管理员");
                    askrecord.setPhone("管理员");
                    askrecord.setDescription("管理员网页查询。" + result);
                    askrecord.setIssuccess(Askrecord.ASK_RESULT_SUCCESS);
                    createAskrecord(askrecord);
                    return result;
                } else{// 需要生成短信任务的
                    String name = "(查询功能:";
                    for (int i = 0; i < functions.size(); i++) {
                        name = name + functions.get(i).getName();
                        if (i + 1 < functions.size()) {
                            name = name + "、";
                        }
                    }
                    name = name + ")";
                    // 将接收者的手机号提取出来
                    String receivers = "";
                    for (int i = 0; i < contactDisplays.size(); i++) {
                        receivers = receivers + contactDisplays.get(i).getPhone();
                        if (i + 1 < contactDisplays.size()) {
                            receivers = receivers + ",";
                        }
                    }
                    if(askType == ASK_TYPE_CLIENT){// 客户端查询（通过手机软件、管理员微信专用网页(TODO)）
                        askrecord.setName("管理员");
                        askrecord.setPhone("管理员");
                        askrecord.setDescription("管理员客户端查询。" + result);
                        askrecord.setIssuccess(Askrecord.ASK_RESULT_SUCCESS);
                        createAskrecord(askrecord);
                        name = "管理员查询并发送" + name;
                    }else if (askType == ASK_TYPE_TRIGGERJOB) {// 定时任务查询
                        askrecord.setName("定时任务查询");
                        askrecord.setPhone("定时任务");
                        askrecord.setDescription("定时任务查询。" + result);
                        askrecord.setIssuccess(Askrecord.ASK_RESULT_SUCCESS);
                        createAskrecord(askrecord);
                        name = "定时任务" + name;
                    }
                    // 创建任务
                    taskService.createTask(name, description, result, receivers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean createAskrecord(Askrecord askrecord) {
        askrecord.setTime(new Date(System.currentTimeMillis()));
        askrecordService.insert(askrecord);
        return true;
    }


    // 未知用户查询，返回提醒
    public String unknownContact(int contactType) {
        if (contactType == ContactServiceImpl.CONTACT_TYPE_OPENID) {
            // 提醒绑定手机号
            return "你尚未绑定手机号，我们无法确定你的身份，所以无法提供服务。请先绑定手机号，发送“SJA。（中文句号）手机号”(例如SJA。13988888888)即可。";
        } else if (contactType == ContactServiceImpl.CONTACT_TYPE_PHONE) {
            // 提醒绑定手机号
            return "我们无法确定你的身份，所以无法提供服务。请先与管理员取得联系，让他帮助你成为合法用户。";
        }
        return null;
    }

    // 用户查询帮助信息
    private String getHelp(int contactType) {
        // 读取帮助信息，从resource中读取 TODO
        if (contactType == ContactServiceImpl.CONTACT_TYPE_OPENID) {
            String s = "1.绑定手机号，请回复：SJA。13988888888；\n";
            s += "2.更改绑定的手机号，请回复：SJU。13988888888；\n";
            s += "3.更改绑定的微信号，请回复：SJO。您收到的验证码；\n";
            s += "4.信息查询，请回复：相关的关键字，如 abc；\n";
            s += "5.查询功能对应的关键字，请回复：关键字。\n";
            return s;
        } else if (contactType == ContactServiceImpl.CONTACT_TYPE_OPENID) {
            String s = "1.信息查询，请回复：相关的关键字，如 abc；\n";
            s += "2.查询功能对应的关键字，请回复：关键字。\n";
            return s;
        }
        return null;
    }

}
