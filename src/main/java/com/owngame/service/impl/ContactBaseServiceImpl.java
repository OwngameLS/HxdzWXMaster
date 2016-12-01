package com.owngame.service.impl;

import com.owngame.dao.ContactBaseDao;
import com.owngame.entity.ContactBase;
import com.owngame.entity.GroupName;
import com.owngame.service.ContactBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-1.
 */
@Service
public class ContactBaseServiceImpl implements ContactBaseService {

    @Autowired
    ContactBaseDao contactBaseDao;

    public ContactBase queryById(long id) {
        return contactBaseDao.queryById(id);
    }

    public ArrayList<ContactBase> queryByHighId(long highid) {
        return contactBaseDao.queryByHighId(highid);
    }

    public ArrayList<ContactBase> queryLikeName(String name) {
        return contactBaseDao.queryLikeName(name);
    }

    public ArrayList<ContactBase> queryByGroup(String groupname) {
        return contactBaseDao.queryByGroup(groupname);
    }

    public ArrayList<ContactBase> queryAll() {
        return contactBaseDao.queryAll();
    }

    public ArrayList<String> getGroups() {
        return contactBaseDao.getGroups();
    }

    public int insert(ContactBase contactBase) {
        return contactBaseDao.insert(contactBase);
    }

    public int update(ContactBase contactBase) {
        return contactBaseDao.update(contactBase);
    }

    public int updateGroup(GroupName groupName) {
        return contactBaseDao.updateGroup(groupName);
    }

    public int updateGroupWithId(GroupName groupName) {
        return contactBaseDao.updateGroupWithId(groupName);
    }

    public int delete(long id) {
        return contactBaseDao.delete(id);
    }

    public int deleteGroup(String groupname) {
        return contactBaseDao.deleteGroup(groupname);
    }

    public int deleteAll() {
        return contactBaseDao.deleteAll();
    }
}
