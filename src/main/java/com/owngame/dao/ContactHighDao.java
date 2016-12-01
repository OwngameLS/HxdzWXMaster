package com.owngame.dao;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.ContactHigh;
import com.owngame.entity.GroupName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-7.
 */
public interface ContactHighDao {

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
