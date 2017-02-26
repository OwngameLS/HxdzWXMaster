package com.owngame.dao;

import com.owngame.entity.ContactBase;
import com.owngame.entity.GroupName;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-7.
 */
public interface ContactBaseDao {

    ContactBase queryById(long id);

    ArrayList<ContactBase> queryByHighId(long highid);

    // 通过姓名查询（模糊查询）
    ArrayList<ContactBase> queryLikeName(String name);

    ArrayList<ContactBase> queryByGroup(String groupname);

    ArrayList<ContactBase> queryByGroupLimit(@Param("offset") int offset, @Param("limit") int limit, @Param("groupname")  String groupname);

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
