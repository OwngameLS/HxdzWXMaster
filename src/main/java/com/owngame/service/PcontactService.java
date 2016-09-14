package com.owngame.service;

import com.owngame.dao.ContactDao;
import com.owngame.entity.Contact;
import com.owngame.entity.GroupName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 当采用上传方式修改通讯录时调用此服务
 * Created by Administrator on 2016/9/10.
 */
@Service
public class PcontactService {
    @Autowired
    ContactDao contactDao;

    /**
     * 处理批量导入联系人
     *
     * @param o
     * @return
     */
    public String doPContacts(Object o) {
        // 从上传文件导入得到的通讯录信息
        ArrayList<Contact> contactsNew = (ArrayList<Contact>) o;
        // 将原数据库中的通讯录信息删除
        contactDao.deleteAll();
        // 插入新数据
        for (Contact contact : contactsNew) {
            contactDao.insert(contact);
        }
        return "OK";
    }

    /**
     * 查询得到所有联系人信息
     *
     * @return
     */
    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = contactDao.queryAll();
        return contacts;
    }

    /**
     * 获得分组信息
     *
     * @return
     */
    public ArrayList<String> getGroups() {
        ArrayList<String> groups = contactDao.getGroups();
        return groups;
    }

    /**
     * 通过分组名获得改组人员信息
     *
     * @param groupnames
     * @return
     */
    public ArrayList<Contact> getContactByGroup(String groupnames) {
        ArrayList<Contact> contacts = contactDao.queryByGroup(groupnames);
        return contacts;
    }

    /**
     * 创建分组信息
     * @param p
     * @return
     */
    public Map<String, Object> insertGroup(Map<String, String> p){
        Map<String, Object> map = new HashMap<String, Object>();
        String groupname = p.get("groupname");
        ArrayList<String> groups = contactDao.getGroups();
        boolean isFound = false;
        for(String s : groups){
            if(s.equals(groupname)){
                isFound = true;
                break;
            }
        }
        if(isFound){// 该分组原来存在过
            // 检查是不是有ids对应的contacts需要修改分组
            String idsString = p.get("ids");
            if(idsString.equals("empty")){
                // 旧分组 又没有ids 跟我闹啥呢！
                map.put("success", "success");
                return map;
            }else{// 有ids，对应更新
                String[] ids = idsString.split(",");
                for (String id:ids){
                    contactDao.updateGroupWithId(new GroupName(Long.parseLong(id), null, groupname));
                }
            }
        }else{// 新的分组
            String idsString = p.get("ids");
            if(idsString.equals("empty")){
                // 没有ids 创建一条空数据
                Contact contact = new Contact();
                contact.setGroupname(groupname);
                contactDao.insert(contact);
            }else{// 有ids，对应更新
                String[] ids = idsString.split(",");
                for (String id:ids){
                    Contact contact = contactDao.queryById(Long.parseLong(id));
                    contact.setGroupname(groupname);
                    // 拿到操作模式
                    String addContactsType = p.get("addContactsType");
                    if(addContactsType.equals("copy")){
                        contact.setId(0);//改id为0，就是等着新增
                        contactDao.insert(contact);
                    }else if(addContactsType.equals("move")){
                        contactDao.update(contact);
                    }
                }
            }
        }
        map.put("success", "success");
        return map;
    }
}
