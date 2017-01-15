package com.owngame.dao;

import com.owngame.entity.Authorization;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-29.
 */
public interface AuthorizationDao {
    Authorization queryById(long id);

    Authorization queryByMac(String mac);

    Authorization queryByUsername(String username);

    Authorization queryUnique(String username, String phone, String mac);

    int deleteUnique(String username, String phone, String mac);

    int deleteById(int id);

    int update(Authorization authorization);

    int insert(Authorization authorization);

    ArrayList<Authorization> queryAll();

    ArrayList<Authorization> queryLikeName(String username);
}
