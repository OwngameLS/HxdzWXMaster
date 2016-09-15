package com.owngame.dao;

import com.owngame.entity.Contact;
import com.owngame.entity.GroupName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-7.
 */
public interface ContactDao {

    Contact queryById(long id);

    // 通过姓名查询（模糊查询）
    ArrayList<Contact> queryLikeName(String name);

    ArrayList<Contact> queryByGroup(String groupname);

    ArrayList<Contact> queryAll();

    ArrayList<String> getGroups();

    int insert(Contact contact);

    int update(Contact contact);

    int updateGroup(GroupName groupName);

    int updateGroupWithId(GroupName groupName);

    int delete(long id);

    int deleteGroup(String groupname);

    int deleteAll();


}
