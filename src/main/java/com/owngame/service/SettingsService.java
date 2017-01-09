package com.owngame.service;

import com.owngame.entity.Settings;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-29.
 */
public interface SettingsService {

    Settings queryById(long id);
    int deleteByName(String name);
    int deleteByReferto(String referto);
    Settings queryByName(String name);
    ArrayList<Settings> queryByReferto(String referto);
    int update(Settings settings);
    int update(Map<String, String> p);
    int insert(Settings settings);
    ArrayList<Settings> queryAll();
    ArrayList<Settings> queryLikeName(String name);
}
