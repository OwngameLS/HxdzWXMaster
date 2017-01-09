package com.owngame.web;

import com.owngame.entity.ContactHigh;
import com.owngame.entity.Settings;
import com.owngame.entity.Task;
import com.owngame.service.*;
import com.owngame.service.impl.AnswerServiceImpl;
import com.owngame.service.impl.ContactServiceImpl;
import com.owngame.service.impl.FunctionServiceImpl;
import com.owngame.service.impl.TaskServiceImpl;
import com.owngame.utils.MachineUtil;
import com.owngame.utils.SecretUtil;
import com.owngame.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-7.
 */

@Controller
@RequestMapping("Smserver/settings")
public class SettingsController {

    @Autowired
    TaskService taskService;
    @Autowired
    SettingsService settingsService;

    /**
     * 获得授权
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
        String serverMac = MachineUtil.getLocalMac();
        // 创建任务 (String name, String description, String contents, String receivers);
        taskService.createTask("authorize request", "授权申请","AR="+username+"="+serverMac, phone);
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
     * 返回所有的设置信息
     * @return
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSettings() {
        System.out.println("getSettings is Called.");
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<Settings> settingses = settingsService.queryAll();
        map.put("settingses", settingses);
        return map;
    }

    /**
     * 通过name查询设置信息
     * @return
     */
    @RequestMapping(value = "/settingslikename", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getSettingsLikeName(@RequestBody Map<String, String> p) {
        System.out.println("getSettingsLikeName is Called.");
        String name = p.get("name");
        Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<Settings> settingses = settingsService.queryLikeName(name + "%");
        map.put("settingses", settingses);
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
        Map<String, Object> map = new HashMap<String, Object>();
        // 先直接检查authorizedState
        Settings authorizedState = settingsService.queryByName("authorizedState");
        if(authorizedState.equals("valid")){// 当它显示为已授权的时候，再去检查是否为用户人为更改
            ArrayList<Settings> settingses = settingsService.queryAll();
            if(settingses == null || settingses.size() == 0){
                settingses = new ArrayList<Settings>();
            }
            authorizedState = getSettingsByName("authorizedState", settingses);
            // 授权到期时间settings
            Settings validTime =  getSettingsByName("validTime", settingses);
            // 授权到期时间加密settings的名称
            String nameOfVTMD5 = SecretUtil.encodeWithMD5("validTime");
            // 授权到期时间加密settings
            Settings validTimeMD5 = getSettingsByName(nameOfVTMD5, settingses);
            if(validTime != null){
                String vt = validTime.getValue();
                String vtMd5 = SecretUtil.encodeWithMD5(vt);
                // 检查是否被人为更改
                if(validTimeMD5.getValue().equals(vtMd5)){
                    // 没有被人为更改 则 检查授权是否到期
                    boolean isExpired = TimeUtil.isExpired(vt);
                    if (isExpired){// 到期了
                        // 先更新数据库
                        authorizedState = getSettingsByName("authorizedState", settingses);
                        authorizedState.setValue("invalid");
                        settingsService.update(authorizedState);
                        String invalidR = "授权到期，请重新获取授权吧。";
                        updateInvalidReason(invalidR, settingses);
                        map.put("invalidReason",invalidR);
                    }
                }else{ // 授权到期时间被人为更改了
                    // 先更新数据库
                    authorizedState = getSettingsByName("authorizedState", settingses);
                    authorizedState.setValue("invalid");
                    settingsService.update(authorizedState);
                    String invalidR = "由于你手动修改了设置信息，当前系统不能继续服务。请重新获取授权吧。";
                    updateInvalidReason(invalidR, settingses);
                    map.put("invalidReason",invalidR);
                }
            }
        }else{
            Settings invalidReason = settingsService.queryByName("invalidReason");
            map.put("invalidReason", invalidReason.getValue());
        }
        map.put("authorizedState", authorizedState);
        return map;
    }

private int updateInvalidReason(String invalidR, ArrayList<Settings> settingses){
    Settings invalidReason = getSettingsByName("invalidReason", settingses);
    invalidReason.setValue(invalidR);
    return settingsService.update(invalidReason);
}

    /**
     * 从已获取的Settingses中按照name读取指定Settings
     * 这样做主要是减轻数据库压力，用内存换磁盘
     * @param name
     * @param settingses
     * @return
     */
    private Settings getSettingsByName(String name, ArrayList<Settings> settingses){
        if(settingses == null || settingses.size()==0){
            return null;
        }
        for(int i=0; i<settingses.size();i++){
            if(settingses.get(i).getName().equals(name)){
                return settingses.get(i);
            }
        }
        return null;
    }


    /**
     * 更新设置
     * @param p
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@RequestBody Map<String, String> p) {
        System.out.println("update settings is Called.");
        int r = settingsService.update(p);

        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        if(r >= 0){
            map.put("success", "success");
        }else{
            map.put("success", "no");
        }
        return map;
    }

    /**
     * 更新设置
     * @param p
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addSettings(@RequestBody Map<String, String> p) {
        System.out.println("addSettings is Called.");
        String description = p.get("description");
        String name = p.get("name");
        String value = p.get("value");
        String referto = p.get("referto");
        String berefered = p.get("berefered");
        Settings settings = new Settings();
        settings.setDescription(description);
        settings.setName(name);
        settings.setValue(value);
        settings.setReferto(referto);
        int result = settingsService.insert(settings);
        Map<String, Object> map = new HashMap<String, Object>();
        if(result > 0){
            Settings settings1 = settingsService.queryByName(name);
            map.put("success", "success");
            map.put("settings", settings1);
        }else{
            map.put("success", "failed");
        }
        return map;
    }

}
