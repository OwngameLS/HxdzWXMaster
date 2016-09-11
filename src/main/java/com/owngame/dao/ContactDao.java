package com.owngame.dao;

import com.owngame.entity.Contact;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-7.
 */
public interface ContactDao {

    ArrayList<Contact> queryByName(String name);

    ArrayList<Contact> queryByGroup(String groupname);

    ArrayList<Contact> queryAll();

    ArrayList<String> getGroups();

    int insert(Contact contact);

    int update(Contact contact);

    int delete(long id);

    int deleteAll();
}
