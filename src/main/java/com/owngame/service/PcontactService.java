package com.owngame.service;

import com.owngame.dao.ContactDao;
import com.owngame.entity.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 当采用上传方式修改通讯录时调用此服务
 * Created by Administrator on 2016/9/10.
 */
@Service
public class PcontactService {
    @Autowired
    ContactDao contactDao;

    public String doPContacts(Object o){
        // 从上传文件导入得到的通讯录信息
        ArrayList<Contact> contactsNew = (ArrayList<Contact>) o;
        // 将原数据库中的通讯录信息删除
        contactDao.deleteAll();
        // 插入新数据
        for(Contact contact:contactsNew){
            contactDao.insert(contact);
        }
        return "OK";
    }

}
