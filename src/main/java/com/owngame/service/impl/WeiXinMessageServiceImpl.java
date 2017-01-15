package com.owngame.service.impl;

import com.owngame.dao.MYUser;
import com.owngame.entity.ContactHigh;
import com.owngame.menu.ManageMenu;
import com.owngame.service.*;
import com.owngame.utils.PhoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.NewsMessage;
import weixin.popular.bean.message.message.NewsMessage.Article;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.util.JsonUtil;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-8-18.
 */
@Service
public class WeiXinMessageServiceImpl implements WeiXinMessageService {
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
    public static final int RETURN_CODE_INVALID_AUTHORIZECODE = -996;// 验证码正确
    public static final int RETURN_CODE_NOT_AUTHORIZE_PHONE = -997;// 手机号未授权
    public static final int RETURN_CODE_INVALID_PHONENUMBER = -998;// 手机号码不合法
    public static final int RETURN_CODE_CHANGE_OPENID = -999;// 绑定的微信号发生变化

    static final String TEXTMSG_PREFIX_PHONENUMBER = "SJ";// 文本消息，手机号逻辑
    static final String TEXTMSG_PREFIX_ADD_PHONENUMBER = "SJA。";// 文本消息，添加手机号
    static final String TEXTMSG_PREFIX_CHANGE_PHONENUMBER = "SJU。";// 文本消息，更新手机号
    static final String TEXTMSG_PREFIX_CHANGE_WXOPENID = "SJO。";// 文本信息，更新绑定微信号

    static final String BATHURL = "http://owngame.ngrok.cc/WeiMaster/";
    static final String SEND_PACKAGE_URL = "sendPackage.jsp?openid=OPENID";

    @Autowired
    AnswerService answerService;
    @Autowired
    ContactService contactService;
    @Autowired
    WeixinAccessTokenService weixinAccessTokenService;
    @Autowired
    ServletContext context;// 获得环境变量的入口！


    String fromUserName; // 消息的发来者，也是返回消息的接收者
    String rtMsgType;// 返回的消息类型
    ContactHigh contactHigh;// 用户详情

    public String handleMessage(Map<String, String> map) {
        // 将传递来的请求数据整理后分析
        System.out.println("map toString :" + map.toString());
        String msgType = map.get("MsgType");
        fromUserName = map.get("FromUserName");
        // 检查用户权限
        contactHigh = contactService.queryHighByOpenId(fromUserName);
        if (MESSAGE_TYPE_TEXT.equals(msgType)) {// 传递来了文本信息
            String content = map.get("Content").trim();
            return handleTextMessage(content);
        } else if (MESSAGE_TYPE_EVENT.equals(msgType)) {// 事件类型消息
            return handleEventMessage(map);
        }
        return null;
    }


    /**
     * 发送客服消息
     *
     * @param message
     * @param openIds
     */
    public void sendTextMessage(String message, String openIds) {
        String openId[] = openIds.split(",");
        for (int i = 0; i < openId.length; i++) {
            String messageJson = initTextMessageOfJsonString(openId[i], message);
            // 调用客服消息借口回复消息
            String token = weixinAccessTokenService.get().getAccesstoken();
            BaseResult br = MessageAPI.messageCustomSend(token, messageJson);
            System.out.println("br:" + br.getErrcode() + "; " + br.getErrmsg());
        }
    }

    /**
     * 处理文本信息，分析文本内容，给出合理应答
     */
    private String handleTextMessage(String content) {
        System.out.println("handleTextMessage is called.");
        rtMsgType = MESSAGE_TYPE_TEXT;
// TODO 先排查一遍是否有微信公众号特殊定义的关键字，方便决定是否需要返回特殊消息
        if (content.startsWith("群发")) {
            String path = context.getServletContextName();
            path = context.getServerInfo();
            return null;
        }


        if (content.startsWith(TEXTMSG_PREFIX_PHONENUMBER)) {// 手机号逻辑
            content = phoneNumberLogic(content);
        } else { // 查询逻辑
            content = answerService.handleAsk(content,
                    FunctionServiceImpl.QUESTIONTYPE_FUNCTION_KEYWORDS,
                    fromUserName,
                    ContactServiceImpl.CONTACT_TYPE_OPENID,
                    AnswerServiceImpl.ASK_TYPE_WX,
                    TaskServiceImpl.SEND_TYPE_WX,
                    "");
        }
        if (rtMsgType.equals(MESSAGE_TYPE_TEXT)) {// 回复文本消息
            return initTextMessageOfJsonString(fromUserName, content);
        }
        return null;
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
        if (WeiXinMessageServiceImpl.MESSAGE_EVENT_SUBSCRIBE.equals(eventType)) {
            // 当用户关注的时候，就要先存下用户的基本信息了啊
            // 获得用户信息并存起来
            MYUser mu = UserHandler.queryUserFromWeixin(fromUserName);
//            UserController.saveUser(mu);
            message = mu.getNickname()
                    + "！\n终于等到你，还好我没放弃~\n ";
            // 检查是否绑定了
            if (contactHigh != null) {// 绑定过手机号
                message += "我发现你以前就关注过我了，这次不要再走丢了哦！\n" +
                        "您的手机号码还是" + contactHigh.getPhone() + "吗？如果不是，请发送【SJU。13988888888】重新告诉我您的号码吧~";
            } else {
                message += answerService.unknownContact(ContactServiceImpl.CONTACT_TYPE_OPENID);
            }
            return initTextMessageOfJsonString(fromUserName, message);
        } else if (WeiXinMessageServiceImpl.MESSAGE_EVENT_CLICK.equals(eventType)) {
            // 根据发送的事件代码返回特定的图文消息，用户通过点击图文消息走向我们的网页吧
            message = handleClickMessage(fromUserName, map);
        } else if (WeiXinMessageServiceImpl.MESSAGE_EVENT_SCANCODE.equals(eventType)) {// 扫码事件
            System.out.println("scan...");
//            message = handleScanMessage(map);
        } else if (WeiXinMessageServiceImpl.MESSAGE_EVENT_VIEW.equals(eventType)) {
            System.out.println("view !");
            message = "vvvv";
        } else if (WeiXinMessageServiceImpl.MESSAGE_EVENT_UNSUBSCRIBE.equals(eventType)) {
            // 用户取消关注，暂时不删除其信息吧
            return null;
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
        return initTextMessageOfJsonString(fromUserName, result);


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
    private String phoneNumberLogic(String content) {
        System.out.println("handle phoneNumberLogic");
        // 提前处理好空白
        content = content.trim();
        // 截取信息 可能是手机号，也可能是验证码
        String info = content.substring(4);
        int r = 0;
        if (content.startsWith(TEXTMSG_PREFIX_ADD_PHONENUMBER)) {// 新增关联手机号
            if (PhoneUtil.isMobile(info) == false) {// 检查手机号码的合理性
                r = RETURN_CODE_INVALID_PHONENUMBER;
            } else {
                r = contactService.updateFromWeixin(info, fromUserName, null);
            }
        } else if (content.startsWith(TEXTMSG_PREFIX_CHANGE_PHONENUMBER)) {// 修改关联手机号
            if (PhoneUtil.isMobile(info) == false) {// 检查手机号码的合理性
                r = RETURN_CODE_INVALID_PHONENUMBER;
            } else {
                r = contactService.updateFromWeixin(info, fromUserName, null);
            }
        } else if (content.startsWith(TEXTMSG_PREFIX_CHANGE_WXOPENID)) {// 修改绑定微信号
            r = contactService.updateFromWeixin(null, fromUserName, info);
        }

        String result = "";
        switch (r) {
            case RETURN_CODE_UNKNOWN:
                result = "非常抱歉，出现了预期外的错误结果，请您再次尝试或告知管理员。";
                break;
            case RETURN_CODE_DATABASE_FAILED:
                result = "非常抱歉，数据库错误，请您再次尝试或告知管理员。";
                break;
            case RETURN_CODE_SUCCESS:
                result = "NICE！操作成功！";
                break;
            case RETURN_CODE_INVALID_AUTHORIZECODE:
                result = "您输入的验证码有误，请您再次尝试或告知管理员。";
                break;
            case RETURN_CODE_NOT_AUTHORIZE_PHONE:
                result = "您输入的手机号码尚未经过管理员审核，请您尝试与管理员联系，让他帮助您解决。";
                break;
            case RETURN_CODE_INVALID_PHONENUMBER:
                result = "您发送的手机号码格式不符合要求，请您检查后再次发送！";
                break;
            case RETURN_CODE_CHANGE_OPENID:
                result = "与这个手机号关联的不是您现在使用的微信号，这个手机号将收到一条短信，里面有验证码，如果你确定要更改绑定的微信号，请将短信中的验证码以‘SJO。123456’的方式发送请求。";
                break;
        }
        return result;
    }


    /**
     * 组装成TextMessage jsonString
     *
     * @param content
     * @return jsonString
     */
    private String initTextMessageOfJsonString(String toUserOpenId, String content) {
        System.out.println("initTextMessageOfJsonString is called");
        content = content + "\n\n回复 “帮助”，可以查看更多文本命令提示哟！/::)";
        TextMessage tm = new TextMessage(toUserOpenId, content);
        return JsonUtil.toJSONString(tm);
    }


    /**
     * 组装成NewsMessage jsonString
     *
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
