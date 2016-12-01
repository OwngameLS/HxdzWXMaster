package com.owngame.service;

import com.owngame.entity.ContactHigh;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-11-30.
 */
public interface ContactHighService {
    ContactHigh queryById(long id);

    ContactHigh queryByOpenId(String openid);

    ContactHigh queryByPhone(String phone);

    ContactHigh queryByBackup(String backup);

    ArrayList<ContactHigh> queryAll();

    int insert(ContactHigh contactHigh);

    int update(ContactHigh contactHigh);

    int delete(long id);

    int deleteAll();
}
