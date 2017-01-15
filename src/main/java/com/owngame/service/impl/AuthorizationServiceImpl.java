package com.owngame.service.impl;

import com.owngame.dao.AuthorizationDao;
import com.owngame.entity.Authorization;
import com.owngame.entity.AuthorizationState;
import com.owngame.entity.Settings;
import com.owngame.service.AuthorizationService;
import com.owngame.service.SettingsService;
import com.owngame.utils.SecretUtil;
import com.owngame.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;


/**
 * Created by Administrator on 2017-1-10.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    @Autowired
    AuthorizationDao authorizationDao;
    @Autowired
    SettingsService settingsService;

    public Authorization queryById(long id) {
        return authorizationDao.queryById(id);
    }

    public Authorization queryByMac(String mac) {
        return authorizationDao.queryByMac(mac);
    }

    public Authorization queryByUsername(String username) {
        return authorizationDao.queryByUsername(username);
    }

    public Authorization queryUnique(String username, String phone, String mac) {
        return authorizationDao.queryUnique(username, phone, mac);
    }

    public int deleteUnique(String username, String phone, String mac) {
        return authorizationDao.deleteUnique(username, phone, mac);
    }

    public int deleteById(int id) {
        return authorizationDao.deleteById(id);
    }

    public int update(Authorization authorization) {
        return authorizationDao.update(authorization);
    }

    public int insert(Authorization authorization) {
        return authorizationDao.insert(authorization);
    }

    public ArrayList<Authorization> queryAll() {
        return authorizationDao.queryAll();
    }

    public ArrayList<Authorization> queryLikeName(String username) {
        return authorizationDao.queryLikeName(username);
    }

    /**
     * 用户注册
     *
     * @param username
     * @param phone
     * @return
     */
    public int userRegist(String username, String phone) {
        Authorization authorization = new Authorization();
        authorization.setUsername(username);
        authorization.setPhone(phone);
        authorization.setExpiration("0");
        return insert(authorization);
    }

    /**
     * 获取授权 用户发出申请
     *
     * @param username
     * @param phone
     * @param mac
     * @return
     */
    public String requestAuthorization(String username, String phone, String mac) {
        // 查看是否缴费了
        Authorization authorization = queryByUsername(username);//, phone, mac);
        if (authorization == null) {
            return "AR--FAILED--nofee";
        } else {
            // 查看是否为同一个用户
            if(authorization.getPhone() == null || authorization.getPhone().equals("null")){
                authorization.setPhone(phone);
            }else{
                if(authorization.getPhone().equals(phone) == false){
                    return "AR--FAILED--duplicate";// 用户申请过了
                }
            }

            if(authorization.getMac() == null || authorization.getMac().equals("null")){
                authorization.setMac(mac);
            }else{
                if(authorization.getMac().equals(mac) == false){
                    return "AR--FAILED--duplicate";// 用户申请过了
                }
            }
            String expiration = authorization.getExpiration();
            if (expiration == null || expiration.equals("null")) {
                return "AR--FAILED--nofee";
            } else {// 检查是否到期了
                boolean isExpired = TimeUtil.isExpired(expiration);
                if (isExpired) {
                    authorization.setExpiration("0");
                    update(authorization);
                    return "AR--FAILED--expiration";//您的授权已经到期，请再次充值后获取授权吧。";
                } else {
                    update(authorization);
                    // 没有过期，将授权及其过期时间返回。
                    return "AR--SUCCESS--" + expiration + "--" + SecretUtil.encodeWithMD5(expiration);
                }
            }
        }
    }

    /**
     * 设置权限(开发者所用）
     *
     * @param infos
     */
    public int setAuthorizationAdmin(String infos) {
//        infos中包含的信息  username ，phone, expiredMonths
        String[] ss = infos.split("==");
        long ex = 1000l * 60 * 60 * 24 * 30 * Long.parseLong(ss[2]);
        // 先查看是否已经有了该用户
        Authorization authorization = queryByUsername(ss[0]);
        long expirationTime = 0;
        boolean isExist = false;
        if (authorization != null) {// 已存在该用户
            isExist = true;
            String expiration = authorization.getExpiration();// 检查是否过期
            if (expiration == null || expiration.equals("null")) {
                expiration = "0";
            }
            boolean isExpired = TimeUtil.isExpired(expiration);
            if (isExpired) {
                // 设置过期时间
                expirationTime = System.currentTimeMillis() + ex;
            } else {
                // 没有过期，将授权及其过期时间返回。
                expirationTime = Long.parseLong(expiration) + ex;
            }
        } else {// 没有找到，新增
            authorization = new Authorization();
            authorization.setUsername(ss[0]);
            expirationTime = System.currentTimeMillis() + ex;
        }
        authorization.setPhone(ss[1]);
        authorization.setExpiration(expirationTime + "");
        if (isExist) {
            return update(authorization);
        } else {
            return insert(authorization);
        }

    }

    /**
     * 设置权限(使用者所用）
     *
     * @param infos
     * @return
     */
    public int setAuthorizationUser(String infos) {
//        "AR--FAILED--您尚未缴费，请充值后再获取授权吧。";
//        "AR--SUCCESS--"+expiration+"--"+ SecretUtil.encodeWithMD5(expiration);
        String[] ss = infos.split("--");
        if (ss[1].equals("FAILED")) {
            Settings settings = settingsService.queryByName("authorizedState");
            settings.setValue("invaild");
            settingsService.update(settings);
            settings = settingsService.queryByName("invalidReason");
            if (ss[2].equals("nofee")) {
                ss[2] = "您尚未缴费，请充值后再获取授权吧。";
            } else if (ss[2].equals("expiration")) {
                ss[2] = "您的授权已经到期，请再次充值后获取授权吧。";
            } else if(ss[2].equals("duplicate")){
                ss[2] = "检测到您重新配置了使用系统的服务器，为了保护版权，我们拒绝这样做，如果实在有需要，请联系技术支持解决。";
            }
            settings.setValue(ss[2]);
            settingsService.update(settings);
            settings = settingsService.queryByName("validTime");
            settings.setValue("0");
            settingsService.update(settings);
            settings = settingsService.queryByName(SecretUtil.encodeWithMD5("validTime"));
            settings.setValue("0");
            settingsService.update(settings);
        } else {
            Settings settings = settingsService.queryByName("authorizedState");
            settings.setValue("valid");
            settingsService.update(settings);
            settings = settingsService.queryByName("invalidReason");
            settings.setValue("no");
            settingsService.update(settings);
            settings = settingsService.queryByName("validTime");
            settings.setValue(ss[2]);
            settingsService.update(settings);
            settings = settingsService.queryByName(SecretUtil.encodeWithMD5("validTime"));
            settings.setValue(ss[3]);
            settingsService.update(settings);
        }
        return 0;
    }

    /**
     * 检查系统的授权状态
     *
     * @return
     */
    public AuthorizationState getAuthorizationState() {
        AuthorizationState authorizationState = new AuthorizationState();
        // 先直接检查authorizedState
        Settings authorizedState = settingsService.queryByName("authorizedState");
        if (authorizedState.getValue().equals("valid")) {// 当它显示为已授权的时候，再去检查是否为用户人为更改
            // 授权到期时间settings
            Settings validTime = settingsService.queryByName("validTime");
            // 授权到期时间加密settings的名称
            String nameOfVTMD5 = SecretUtil.encodeWithMD5("validTime");
            // 授权到期时间加密settings
            Settings validTimeMD5 = settingsService.queryByName(nameOfVTMD5);
            if (validTime != null) {
                String vt = validTime.getValue();
                String vtMd5 = SecretUtil.encodeWithMD5(vt);
                // 检查是否被人为更改
                if (validTimeMD5.getValue().equals(vtMd5)) {
                    // 没有被人为更改 则 检查授权是否到期
                    boolean isExpired = TimeUtil.isExpired(vt);
                    if (isExpired) {// 到期了
                        // 先更新数据库
                        authorizedState.setValue("invalid");
                        settingsService.update(authorizedState);
                        String invalidR = "授权到期，请重新获取授权吧。";
                        updateInvalidReason(invalidR);
                        authorizationState.setValid("invalid");
                        authorizationState.setInvalidReason(invalidR);//http://www.huajiao.com/l/69320530
                    }else {
                        authorizationState.setValid("valid");
                    }
                } else { // 授权到期时间被人为更改了
                    // 先更新数据库
                    authorizedState.setValue("invalid");
                    settingsService.update(authorizedState);
                    String invalidR = "由于你手动修改了设置信息，当前系统不能继续服务。请重新获取授权吧。";
                    updateInvalidReason(invalidR);
                    authorizationState.setValid("invalid");
                    authorizationState.setInvalidReason(invalidR);
                }
            }
        } else {
            Settings invalidReason = settingsService.queryByName("invalidReason");
            authorizationState.setValid("invalid");
            authorizationState.setInvalidReason(invalidReason.getValue());
        }
        return authorizationState;
    }

    // 更新授权失败的原因
    private int updateInvalidReason(String invalidR) {
        Settings invalidReason = settingsService.queryByName("invalidReason");
        invalidReason.setValue(invalidR);
        return settingsService.update(invalidReason);
    }
}
