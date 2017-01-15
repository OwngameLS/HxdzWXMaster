package com.owngame.web;

import com.owngame.entity.Settings;
import com.owngame.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    SettingsService settingsService;

    /**
     * 返回所有的设置信息
     *
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
     *
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
     * 更新设置
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@RequestBody Map<String, String> p) {
        System.out.println("update settings is Called.");
        Map<String, Object> map = new HashMap<String, Object>();
        int r = settingsService.update(p);
        // 返回更新后的该组信息
        if (r >= 0) {
            map.put("success", "success");
        } else {
            map.put("success", "no");
        }
        return map;
    }

    /**
     * 新增设置
     *
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
        if (result > 0) {
            Settings settings1 = settingsService.queryByName(name);
            map.put("success", "success");
            map.put("settings", settings1);
        } else {
            map.put("success", "failed");
        }
        return map;
    }

}
