package com.owngame.service.impl;

import com.owngame.dao.SettingsDao;
import com.owngame.entity.Settings;
import com.owngame.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-29.
 */
@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    SettingsDao settingsDao;

    @Override
    public Settings queryById(long id) {
        return settingsDao.queryById(id);
    }

    @Override
    public int deleteByName(String name) {
        return settingsDao.deleteByName(name);
    }

    @Override
    public int deleteByReferto(String referto) {
        return settingsDao.deleteByReferto(referto);
    }

    @Override
    public Settings queryByName(String name) {
        return settingsDao.queryByName(name);
    }

    @Override
    public ArrayList<Settings> queryByReferto(String referto) {
        return settingsDao.queryByReferto(referto);
    }

    @Override
    public int update(Settings settings) {
        return settingsDao.update(settings);
    }

    @Override
    public int update(Map<String, String> p) {
        String action = p.get("action");
        String name = p.get("name");
        String value = p.get("value");
        Settings settings = queryByName(name);
        if(action.equals("delete")){
            int r = 0;
            if(settings.getReferto().equals("self")){// 删除与自己关联的其他设置
                r = settingsDao.deleteByReferto(name);
            }
            if(r >= 0) {
                // 删除自己
             return settingsDao.deleteByName(name);
            }
        }else if(action.equals("update")){
            settings.setValue(value);
            return settingsDao.update(settings);
        }
        return 0;
    }

    @Override
    public int insert(Settings settings) {
        return settingsDao.insert(settings);
    }

    @Override
    public ArrayList<Settings> queryAll() {
        return settingsDao.queryAll();
    }

    @Override
    public ArrayList<Settings> queryLikeName(String name) {
        return settingsDao.queryLikeName(name);
    }
}
