package com.owngame.service;

import com.owngame.entity.Authorization;
import com.owngame.entity.AuthorizationState;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-1-10.
 */
public interface AuthorizationService {
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

    int userRegist(String username, String phone);

    String requestAuthorization(String username, String phone, String mac);

    int setAuthorizationAdmin(String infos);

    int setAuthorizationUser(String infos);

    AuthorizationState getAuthorizationState();
}
