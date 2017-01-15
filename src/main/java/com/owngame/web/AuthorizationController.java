package com.owngame.web;

import com.owngame.entity.Authorization;
import com.owngame.entity.AuthorizationState;
import com.owngame.entity.Settings;
import com.owngame.service.AuthorizationService;
import com.owngame.service.SettingsService;
import com.owngame.service.TaskService;
import com.owngame.utils.MachineUtil;
import com.owngame.utils.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2016-11-7.
 */

@Controller
@RequestMapping("Smserver/authorization")
public class AuthorizationController {

    @Autowired
    TaskService taskService;
    @Autowired
    SettingsService settingsService;
    @Autowired
    AuthorizationService authorizationService;

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1[3|4|5|8]\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isNum(String str) throws PatternSyntaxException {
        String regExp = "^\\+?[1-9][0-9]*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检测用户名
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/checkusername", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkUsername(@RequestBody Map<String, String> p) {
        System.out.println("checkUsername is Called.");
        String username = p.get("username");
        Authorization authorization = authorizationService.queryByUsername(username);
        Map<String, Object> map = new HashMap<String, Object>();
        if (authorization != null) {// 这个用户名存在
            map.put("success", "failed");
            // 找到类似的用户名
            ArrayList<Authorization> authorizations = authorizationService.queryLikeName("%" + username + "%");
            String similarNames = "";
            for (int i = 0; i < authorizations.size(); i++) {
                if (authorizations.get(i).getUsername().equals(username) == false) {
                    similarNames += authorizations.get(i).getUsername();
                    if (i + 1 < authorizations.size()) {
                        similarNames += ",";
                    }
                }
            }
            if (similarNames.equals("") == false) {
                similarNames = "类似的名称还有：" + similarNames;
            }
            map.put("similarNames", similarNames);
        } else {
            map.put("success", "success");
        }
        return map;
    }

    /**
     * 管理员登录
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/loginAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginAdmin(@RequestBody Map<String, String> p) {
        System.out.println("loginAdmin is Called.");
        Map<String, Object> map = new HashMap<String, Object>();
        String password = p.get("password");
        String passwordMD5 = SecretUtil.encodeWithMD5(password);
        Settings passwordSt = settingsService.queryByName("password");
        if (passwordMD5.equals(passwordSt.getValue())) {
            // 密码验证成功
            map.put("success", "success");
            // 返回组件html
            String htmlStr = "<form id=\"grantfrom\" role=\"form\" action=\"abc\" method=\"post\">" +
                    "<div class=\"row\">" +
                    "<div class=\"col-md-4 text-right\">用户名</div>" +
                    "<div class=\"col-md-2 text-left\"><input id=\"adminusername\" name=\"adminusername\" class=\"form-control\" /></div>" +
                    "</div>" +
                    "<div class=\"row\">" +
                    "<div class=\"col-md-4 text-right\">手机号</div>" +
                    "<div class=\"col-md-2 text-left\"><input id=\"adminphone\" name=\"adminphone\" class=\"form-control\" /></div>" +
                    "</div>" +
                    "<div class=\"row\">" +
                    "<div class=\"col-md-4 text-right\">授权时长</div>" +
                    "<div class=\"col-md-2 text-left\"><input id=\"adminmonth\" name=\"adminmonth\" class=\"form-control\" /></div>" +
                    "</div>" +
                    "<div class=\"row\">" +
                    "<div class=\"col-md-4 text-right\"></div>" +
                    "<div class=\"col-md-4 text-right\"><button type=\"button\" class=\"btn bg-primary\" id=\"loginAdmin\" onclick=\"authorizeAdmin()\">授权</button></div>" +
                    "</div>" +
                    "</form>" +
                    "<div class=\"row\">" +
                    "<div class=\"col-md-2 text-right\"></div>" +
                    "<div class=\"col-md-4 text-center\" id=\"grantresult\"></div>" +
                    "</div>";
            map.put("htmlStr", htmlStr);
            map.put("action", "Smserver/authorization/grant");
        } else {
            map.put("success", "failed");
        }
        return map;
    }

    /**
     * 给予授权（开发者所用）
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/grant", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> grant(@RequestBody Map<String, String> p) {
        System.out.println("grant is Called." + p.toString());
        try {
            String adminusername = p.get("adminusername").trim();
            String adminphone = p.get("adminphone").trim();
            String adminmonth = p.get("adminmonth").trim();
            System.out.println("adminusername:" + adminusername + ";adminphone:" + adminphone + ";adminmonth:" + adminmonth);
            Map<String, Object> map = new HashMap<String, Object>();
            // 错误检查
            if (adminusername.equals("") || adminusername.equals("null")) {
                map.put("success", "failed");
                map.put("failedObject", "adminusername");
                map.put("failedReason", "输入内容不能为空！");
                return map;
            }
            if (adminphone.equals("") || adminphone.equals("null")) {
                map.put("success", "failed");
                map.put("failedObject", "adminphone");
                map.put("failedReason", "输入内容不能为空！");
                return map;
            }
            if (isChinaPhoneLegal(adminphone) == false) {
                map.put("success", "failed");
                map.put("failedObject", "adminphone");
                map.put("failedReason", "手机号码不符合规则！");
                return map;
            }
            if (adminmonth.equals("") || adminmonth.equals("null")) {
                map.put("success", "failed");
                map.put("failedObject", "adminmonth");
                map.put("failedReason", "输入内容不能为空！");
                return map;
            }
            if (isNum(adminmonth) == false) {
                map.put("success", "failed");
                map.put("failedObject", "adminmonth");
                map.put("failedReason", "月份必须输入正整数！");
                return map;
            }
            // 都没有问题，开始进行授权逻辑
            int result = authorizationService.setAuthorizationAdmin(adminusername + "==" + adminphone + "==" + adminmonth);
            if (result >= 0) {
                map.put("success", "success");
            } else {
                map.put("success", "failed");
                map.put("failedObject", "grantresult");
                map.put("failedReason", "数据库操作失败。");
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用户申请
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> regist(@RequestBody Map<String, String> p) {
        System.out.println("regist is Called.");
        // 去申请授权的手机号
        String phone = p.get("phone");
        String username = p.get("username");
        int result = authorizationService.userRegist(username, phone);
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        if (result >= 0) {
            map.put("success", "success");
        } else {
            map.put("success", "failed");
        }
        return map;
    }


    /**
     * 获得授权
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/requestAuthorization", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> requestAuthorization(@RequestBody Map<String, String> p) {
        System.out.println("getAuthorization is Called.");
        // 去申请授权的手机号
        String phone = p.get("phone");
        String username = p.get("username");
        String userphone = p.get("userphone");
        String serverMac = MachineUtil.getLocalMac();
        // 创建任务 (String name, String description, String contents, String receivers);
        taskService.createTask("authorize request", "授权申请", "AR--" + username + "--" + userphone + "--" + serverMac, phone);
        Settings invalidReason = settingsService.queryByName("invalidReason");
        invalidReason.setValue("授权申请已发出，请等待处理。你也可以直接联系技术支持。");
        settingsService.update(invalidReason);
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("success", "success");
        map.put("invalidReason", invalidReason.getValue());
        return map;
    }


    /**
     * 返回授权状态信息
     *
     * @return
     */
    @RequestMapping(value = "/authorizedState", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAuthorizedState() {
        System.out.println("getAuthorizedState is Called.");
        AuthorizationState authorizationState = authorizationService.getAuthorizationState();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("authorizedState", authorizationState.getValid());
        map.put("invalidReason", authorizationState.getInvalidReason());
        System.out.println(map.toString());
        return map;
    }


}
