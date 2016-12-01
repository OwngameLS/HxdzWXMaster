package com.owngame.dao;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.GroupName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-7.
 */
public interface ContactDao {

    ContactDisplay queryById(long id);

    ContactDisplay queryByOpenId(String openid);

    ContactDisplay queryByPhone(String phone);

    ContactDisplay queryByBackup(String backup);

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
