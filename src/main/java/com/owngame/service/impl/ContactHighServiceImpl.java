package com.owngame.service.impl;

import com.owngame.dao.ContactHighDao;
import com.owngame.entity.ContactHigh;
import com.owngame.service.ContactHighService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-1.
 */
@Service
public class ContactHighServiceImpl implements ContactHighService {
    @Autowired
    ContactHighDao contactHighDao;

    public ContactHigh queryById(long id) {
        return contactHighDao.queryById(id);
    }

    public ContactHigh queryByOpenId(String openid) {
        return contactHighDao.queryByOpenId(openid);
    }

    public ContactHigh queryByPhone(String phone) {
        return contactHighDao.queryByPhone(phone);
    }

    public ContactHigh queryByBackup(String backup) {
        return contactHighDao.queryByBackup(backup);
    }

    public ArrayList<ContactHigh> queryAll() {
        return contactHighDao.queryAll();
    }

    public int insert(ContactHigh contactHigh) {
        // 1.现根据phone查找
        ContactHigh contactHigh1 = queryByPhone(contactHigh.getPhone());
        if (contactHigh1 != null) {
            // 说明找到了，只需要更新
            contactHigh1.setGrade(contactHigh.getGrade());
            int r = update(contactHigh1);
            if (r >= 0) {
                return (int) contactHigh1.getId();
            }
        }
        return contactHighDao.insert(contactHigh);
    }

    public int update(ContactHigh contactHigh) {
        // 判断是否需要变成插入
        if (contactHigh.getId() <= 0) {
            return contactHighDao.insert(contactHigh);
        }
        return contactHighDao.update(contactHigh);
    }

    public int delete(long id) {
        return contactHighDao.delete(id);
    }

    public int deleteAll() {
        return contactHighDao.deleteAll();
    }
}
