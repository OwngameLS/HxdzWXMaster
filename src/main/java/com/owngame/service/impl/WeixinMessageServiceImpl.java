package com.owngame.service.impl;

import com.owngame.dao.ContactDao;
import com.owngame.dao.MYUser;
import com.owngame.entity.Contact;
import com.owngame.menu.ManageMenu;
import com.owngame.service.*;
import com.owngame.utils.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-8-18.
 */
@Service
public class WeixinMessageServiceImpl implements WeixinMessageService{
    // 消息类型
    public static final String MESSAGE_TYPE_TEXT = "text";
    public static final String MESSAGE_TYPE_NEWS = "news";
    public static final String MESSAGE_TYPE_IMAGE = "image";
    public static final String MESSAGE_TYPE_VOICE = "voice";
    public static final String MESSAGE_TYPE_MUSIC = "music";
    public static final String MESSAGE_TYPE_VIDEO = "video";
    public static final String MESSAGE_TYPE_LINK = "link";
    public static final String MESSAGE_TYPE_LOCATION = "location";
    public static final String MESSAGE_TYPE_EVENT = "event";
    public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";
    public static final String MESSAGE_EVENT_UNSUBSCRIBE = "unsubscribe";
    public static final String MESSAGE_EVENT_CLICK = "CLICK";
    public static final String MESSAGE_EVENT_VIEW = "VIEW";
    public static final String MESSAGE_EVENT_SCANCODE = "scancode_push";
    // static final String EVENTKEY
    // 全局返回值
    public static final int RETURN_CODE_UNKNOWN = -1;// 未知结果
    public static final int RETURN_CODE_DATABASE_FAILED = -2;// 数据库操作错误
    public static final int RETURN_CODE_SUCCESS = 0;// OK
    public static final int RETURN_CODE_INVALID_PHONENUMBER = 1;// 手机号码不合法
    static final String TEXTMSG_PREFIX_PHONENUMBER = "SJ";// 文本消息，手机号逻辑
    static final String TEXTMSG_PREFIX_ADD_PHONENUMBER = "SJA=";// 文本消息，添加手机号
    static final String TEXTMSG_PREFIX_CHANGE_PHONENUMBER = "SJU=";// 文本消息，更新手机号
    static final String BATHURL = "http://owngame.ngrok.cc/WeiMaster/";
    static final String SEND_PACKAGE_URL = "sendPackage.jsp?openid=OPENID";

    @Autowired
    FunctionService functionService;

    ContactDao contactDao;

    String fromUserName; // 消息的发来者，也是返回消息的接收者
    String rtMsgType;// 返回的消息类型

    Contact contact;// 用户详情

    public String handleMessage(Map<String, String> map) {
        // 将传递来的请求数据整理后分析
        System.out.println("map toString :" + map.toString());
        String msgType = map.get("MsgType");
        fromUserName = map.get("FromUserName");
        // 检查用户权限
        contact = contactDao.queryByOpenId(fromUserName);
        if (WeixinMessageServiceImpl.MESSAGE_TYPE_TEXT.equals(msgType)) {// 传递来了文本信息
            String content = map.get("Content");
            return handleTextMessage(content);
        } else if (WeixinMessageServiceImpl.MESSAGE_TYPE_EVENT.equals(msgType)) {// 事件类型消息
            return handleEventMessage(map);
        }
        return null;
    }

    /**
     * 处理文本信息，分析文本内容，给出合理应答
     */
    private String handleTextMessage(String content) {
        System.out.println("handleTextMessage is called.");
        rtMsgType = MESSAGE_TYPE_TEXT;
// TODO 先排查一遍是否有微信公众号特殊定义的关键字，方便决定是否需要返回特殊消息
//        if(content.startsWith())
        if (content.startsWith(TEXTMSG_PREFIX_PHONENUMBER)) {// 手机号逻辑
            int rcode = phoneNumberLogic(fromUserName, content);
            if (rcode == RETURN_CODE_SUCCESS) {
                content = "操作成功！";
            } else {
                content = "操作失败咯，再试试？多次失败，请稍后再试吧~";
            }
        }
        if(contact != null) {
            content = functionService.getFunctionResultsByKeywords(contact.getGrade(), content);
        }else{// 没有查询到用户绑定情况
            content = returnAskBindPhone();
        }
        if(rtMsgType.equals(MESSAGE_TYPE_TEXT)){// 回复文本消息
            return initTextMessageOfJsonString(content);
        }
        return null;
    }

    // 提醒绑定手机号
    private String returnAskBindPhone(){
        return "由于你尚未绑定手机号，我们无法确定你的身份，所以无法提供服务。请先绑定手机号，发送“SJA手机号”(例如SJA13988888888)即可。";
    }

    /**
     * 处理事件类消息的具体方法
     *
     * @param map
     * @return
     */
    private String handleEventMessage(Map<String, String> map) {
        String eventType = map.get("Event");// 获得事件类型
        String fromUserName = map.get("FromUserName");
        String message = null;
        if (WeixinMessageServiceImpl.MESSAGE_EVENT_SUBSCRIBE.equals(eventType)) {
            // 当用户关注的时候，就要先存下用户的基本信息了啊
            // 获得用户信息并存起来
            MYUser mu = UserHandler.queryUserFromWeixin(fromUserName);
//            UserController.saveUser(mu);
            message = mu.getNickname()
                    + "！\n终于等到你，还好我没放弃~\n 请回复“101=手机号”（如101=1398888888）的方式发送你的手机号码给我，方便你收发包裹时我给你发短息啊！";
            return initTextMessageOfJsonString(message);
        } else if (WeixinMessageServiceImpl.MESSAGE_EVENT_CLICK.equals(eventType)) {
            // 根据发送的事件代码返回特定的图文消息，用户通过点击图文消息走向我们的网页吧
            message = handleClickMessage(fromUserName, map);
        } else if (WeixinMessageServiceImpl.MESSAGE_EVENT_SCANCODE.equals(eventType)) {// 扫码事件
            System.out.println("scan...");
//            message = handleScanMessage(map);
        } else if (WeixinMessageServiceImpl.MESSAGE_EVENT_VIEW.equals(eventType)) {
            System.out.println("view !");
            message = "vvvv";
        }
        return message;
    }

   /* *//**
     * 处理扫描二维码事件
     * 只需将事件存入待处理的队列中即可啦
     * @param map
     *//*
    private static String handleScanMessage(Map<String, String> map) {
        String message = null;
        String scanresult = map.get("ScanResult");
        String packageid = "";
        if(scanresult.contains("packageId")){
            int index = scanresult.indexOf("packageId") + 10;
            packageid = scanresult.substring(index);
        }else{
            return "您刚扫描的二维码不是我能处理的哦，不过已经交给微信来处理啦！";
        }
        String eventKey = map.get("EventKey");
        String createTime = map.get("CreateTime");
        QREventWaiter qrew = new QREventWaiter(packageid, eventKey, createTime);
        WeiServlet.mqrController.addQREvent(qrew);
        message = "notNeed";
        return message;
    }*/

    /**
     * 处理点击菜单触发的消息事件
     *
     * @param fromUserName
     * @param map
     */
    private String handleClickMessage(String fromUserName,
                                             Map<String, String> map) {
        String eventKey = map.get("EventKey");
        System.out.println("eventKey:" + eventKey);
        String result = null;
        if (eventKey.startsWith(ManageMenu.EVENTKEY_QUERY_PREFIX)) {
            result = QueryService.handleQuery(eventKey);
        }
        return initTextMessageOfJsonString(result);


//
//        if (MessageHandler.EVENTKEY_SEND_PACKAGE.equals(eventKey)) {
//            String url = BATHURL + SEND_PACKAGE_URL;
//            url = url.replace("OPENID", fromUserName);
//            return initNewsOfJsonString(fromUserName, url);
//        }else if(MessageHandler.EVENTKEY_PACKAGE_ONTHEWAY.equals(eventKey)){
//            return initTextOfJsonString(fromUserName,"查询中！请稍后...");
//        }
//        return null;
    }

    /**
     * 处理手机号相关逻辑
     *
     * @param content
     * @return
     */
    private int phoneNumberLogic(String fromUserName, String content) {
        System.out.println("handle phoneNumberLogic");
        // 提前处理好空白
        String word = content.replaceAll(" ", "");
        if (word.startsWith(TEXTMSG_PREFIX_ADD_PHONENUMBER)) {// 新增关联手机号
            word = content.replaceAll("^" + TEXTMSG_PREFIX_ADD_PHONENUMBER, "")
                    .trim();
        } else if (word.startsWith(TEXTMSG_PREFIX_CHANGE_PHONENUMBER)) {// 修改关联手机号
            word = content.replaceAll("^" + TEXTMSG_PREFIX_CHANGE_PHONENUMBER,
                    "").trim();
        }
        // 检查手机号码的合理性
        if (PhoneUtil.isMobile(word)) {
            // 更新操作
            MYUser mu = UserHandler.queryUserById(fromUserName,
                    UserHandler.IDTYPE_OPEN);
            if (mu != null) {// 查询到了就更新一个字段
                mu.setPhonenumber(word);
            } else {
                mu = UserHandler.queryUserFromWeixin(fromUserName);
                mu.setPhonenumber(word);
            }
            boolean r = UserHandler.saveUser(mu);
            if (r) {
                return RETURN_CODE_SUCCESS;
            } else {
                return RETURN_CODE_DATABASE_FAILED;
            }
        } else {
            return RETURN_CODE_INVALID_PHONENUMBER;
        }
    }


    /**
     * 组装成TextMessage jsonString
     * @param content
     * @return jsonString
     */
    private String initTextMessageOfJsonString(String content){
        System.out.println("initTextMessageOfJsonString is called");
        content = content + "\n\n回复 “帮助”，可以查看更多文本命令提示哟！/::)";
        TextMessage tm = new TextMessage(fromUserName, content);
        return JsonUtil.toJSONString(tm);
    }



    /**
     * 组装成NewsMessage jsonString
     * @param urlInfo
     * @return jsonString
     */
    public String initNewsMessageOfJsonString(String urlInfo) {
        System.out.println("initNewsOfJsonString is called");
        // 生成article
        String title = "发货！";
        String description = "点我发货";
        String url = urlInfo;
        String picurl = "http://mmbiz.qpic.cn/mmbiz/qwbuqiced74EWqlCdJlUSustTh9Ec8jZbmEe3SgAns6PojicXdwv44Mq3EWibelHbXpTjnRouOr4A1IoicUtjbxItQ/0";
        Article a = new Article(title, description, url, picurl);
        List<Article> articles = new ArrayList<Article>();
        articles.add(a);
        NewsMessage nm = new NewsMessage(fromUserName, articles);
        return JsonUtil.toJSONString(nm);
    }

}
