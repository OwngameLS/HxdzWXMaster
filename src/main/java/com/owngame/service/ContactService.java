package com.owngame.service;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.ContactHigh;
import com.owngame.entity.GroupName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-11-28.
 */
public interface ContactService {

    int updateFromWeixin(String phone, String openid, String identifyingCode);

    ContactDisplay queryById(long id);

    ArrayList<ContactDisplay> queryDisplayByOpenId(String openid);

    ContactHigh queryHighByOpenId(String openid);

    ArrayList<ContactDisplay> queryDisplayByPhone(String phone);

    ContactHigh queryHighByPhone(String phone);

    ArrayList<ContactDisplay> queryDisplayByBackup(String backup);

    ContactHigh queryHighByBackup(String backup);

    // 通过姓名查询（模糊查询）
    ArrayList<ContactDisplay> queryLikeName(String name);

    ArrayList<ContactDisplay> queryByGroup(String groupname);

    ArrayList<ContactDisplay> queryAll();

    ArrayList<String> getGroups();

    int insert(ContactDisplay contactDisplay);

    int update(ContactDisplay contactDisplay);

    int updateGroup(GroupName groupName);

    int updateGroupWithId(GroupName groupName);

    int delete(long id);

    int deleteGroup(String groupname);

    int deleteAll();
}
