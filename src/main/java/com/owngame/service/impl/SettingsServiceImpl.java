package com.owngame.service.impl;

import com.owngame.dao.SettingsDao;
import com.owngame.entity.Settings;
import com.owngame.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    public Settings queryByName(String name) {
        return settingsDao.queryByName(name);
    }

    @Override
    public ArrayList<Settings> queryByRequire(String require) {
        return settingsDao.queryByRequire(require);
    }

    @Override
    public int update(Settings settings) {
        return settingsDao.update(settings);
    }

    @Override
    public ArrayList<Settings> queryAll() {
        return settingsDao.queryAll();
    }
}