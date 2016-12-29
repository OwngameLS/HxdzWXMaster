package com.owngame.service;

import com.owngame.entity.Settings;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-29.
 */
public interface SettingsService {

    Settings queryById(long id);
    int deleteByName(String name);
    Settings queryByName(String name);
    ArrayList<Settings> queryByRequire(String require);
    int update(Settings settings);
    ArrayList<Settings> queryAll();
}
