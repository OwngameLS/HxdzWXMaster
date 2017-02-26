package com.owngame.service;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.ContactHigh;
import com.owngame.entity.GroupName;
import com.owngame.entity.Pager;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-28.
 */
public interface ContactService {

    int updateFromWeixin(String phone, String openid, String identifyingCode);

    ContactDisplay queryById(long id);

    String batchUpdateContacts(Object o);

    boolean initContactsFile(String filePath);

    ContactHigh queryHighByOpenId(String openid);

    ContactHigh queryHighByPhone(String phone);

    ContactHigh queryHighByBackup(String backup);

    ArrayList<ContactDisplay> queryDisplayByInfos(String contactInfos, int infoType);

    ArrayList<ContactDisplay> queryDisplayByPhone(String phone);

    ArrayList<ContactDisplay> queryDisplayByOpenId(String openid);

    ArrayList<ContactDisplay> queryDisplayByBackup(String backup);

    ArrayList<ContactDisplay> queryAll();

    // 通过姓名查询（模糊查询）
    ArrayList<ContactDisplay> queryLikeName(String name);

    ArrayList<ContactDisplay> queryByGroup(String groupname);

    Pager<ContactDisplay> queryByGroupLimit(int pageSize, int targetPage, String groupname);

    ArrayList<ContactDisplay> getContactByIds(String ids);

    Map<String, Object> insertGroup(Map<String, String> p);

    ArrayList<String> getGroups();

    int insert(ContactDisplay contactDisplay);

    int update(ContactDisplay contactDisplay);

    int updateGroup(GroupName groupName);

    int updateGroupWithId(GroupName groupName);

    int delete(long id);

    int deleteGroup(String groupname);

    int deleteAll();
}
