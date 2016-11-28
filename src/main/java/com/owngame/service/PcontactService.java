package com.owngame.service;

import com.owngame.dao.ContactDao;
import com.owngame.entity.Contact;
import com.owngame.entity.GroupName;
import com.owngame.utils.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
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
    ContactService contactService;

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
        contactService.deleteAll();
        // 插入新数据
        for (Contact contact : contactsNew) {
            contactService.insert(contact);
        }
        return "OK";
    }

    /**
     * 查询得到所有联系人信息
     *
     * @return
     */
    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = contactService.queryAll();
        return contacts;
    }

    /**
     * 获得分组信息
     *
     * @return
     */
    public ArrayList<String> getGroups() {
        ArrayList<String> groups = contactService.getGroups();
        return groups;
    }

    /**
     * 通过分组名获得改组人员信息
     *
     * @param groupnames
     * @return
     */
    public ArrayList<Contact> getContactByGroup(String groupnames) {
        ArrayList<Contact> contacts = contactService.queryByGroup(groupnames);
        return contacts;
    }

    /**
     * 创建分组信息
     *
     * @param p
     * @return
     */
    public Map<String, Object> insertGroup(Map<String, String> p) {
        Map<String, Object> map = new HashMap<String, Object>();
        String groupname = p.get("groupname");
        ArrayList<String> groups = contactService.getGroups();
        boolean isFound = false;
        for (String s : groups) {
            if (s.equals(groupname)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {// 该分组原来存在过
            // 检查是不是有ids对应的contacts需要修改分组
            String idsString = p.get("ids");
            if (idsString.equals("empty")) {
                // 旧分组 又没有ids 跟我闹啥呢！
                map.put("success", "success");
                return map;
            } else {// 有ids，对应更新
                String[] ids = idsString.split(",");
                for (String id : ids) {
                    contactService.updateGroupWithId(new GroupName(Long.parseLong(id), null, groupname));
                }
            }
        } else {// 新的分组
            String idsString = p.get("ids");
            if (idsString.equals("empty")) {
                // 没有ids 创建一条空数据
                Contact contact = new Contact();
                contact.setGroupname(groupname);
                contactService.insert(contact);
            } else {// 有ids，对应更新
                String[] ids = idsString.split(",");
                for (String id : ids) {
                    Contact contact = contactService.queryById(Long.parseLong(id));
                    contact.setGroupname(groupname);
                    // 拿到操作模式
                    String addContactsType = p.get("addContactsType");
                    if (addContactsType.equals("copy")) {
                        contact.setId(0);//改id为0，就是等着新增
                        contactService.insert(contact);
                    } else if (addContactsType.equals("move")) {
                        contactService.update(contact);
                    }
                }
            }
        }
        map.put("success", "success");
        return map;
    }

    /**
     * 根据ids获取联系人信息
     *
     * @param ids
     * @return
     */
    public ArrayList<Contact> getContactByIds(String ids) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        String[] idArray = ids.split(",");
        for (String s : idArray) {
            Contact contact = contactService.queryById(Long.parseLong(s));
            if (contact != null) {
                contacts.add(contact);
            }
        }
        System.out.println("contacts.size:" + contacts.size());
        return contacts;
    }


    // 将已经编辑好的联系人信息存储成Excel文件，并返回给调用者（MainController）用于下载
    public boolean initContactsFile(String filePath){
        // 1.拿到所有联系人信息
        ArrayList<Contact> contacts = contactService.queryAll();
        if(contacts == null || contacts.size()==0){
            Contact contact = new Contact();
            contacts.add(contact);
        }
        return ExcelUtil.initContactsFile(filePath, contacts);
    }

}
