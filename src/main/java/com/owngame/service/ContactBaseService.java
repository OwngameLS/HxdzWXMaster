package com.owngame.service;

import com.owngame.entity.ContactBase;
import com.owngame.entity.GroupName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-11-30.
 */
public interface ContactBaseService {

    ContactBase queryById(long id);

    ArrayList<ContactBase> queryByHighId(long highid);

    // 通过姓名查询（模糊查询）
    ArrayList<ContactBase> queryLikeName(String name);

    ArrayList<ContactBase> queryByGroup(String groupname);

    ArrayList<ContactBase> queryByGroupLimit(int pageSize, int targetPage, String groupname);

    int countAllByGroup(String groupname);

    ArrayList<ContactBase> queryAll();

    ArrayList<String> getGroups();

    int insert(ContactBase contactBase);

    int update(ContactBase contactBase);

    int updateGroup(GroupName groupName);

    int updateGroupWithId(GroupName groupName);

    int delete(long id);

    int deleteGroup(String groupname);

    int deleteAll();
}
