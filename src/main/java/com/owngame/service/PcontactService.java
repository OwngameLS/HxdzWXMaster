package com.owngame.service;

import com.owngame.entity.ContactDisplay;
import com.owngame.entity.GroupName;
import com.owngame.utils.ExcelUtil;
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
    ContactService contactService;

    /**
     * 处理批量导入联系人
     *
     * @param o
     * @return
     */
    public String doPContacts(Object o) {
        // 从上传文件导入得到的通讯录信息
        ArrayList<ContactDisplay> contactsNew = (ArrayList<ContactDisplay>) o;
        // 将原数据库中的通讯录信息删除
        contactService.deleteAll();
        // 插入新数据
        for (ContactDisplay contactDisplay : contactsNew) {
            contactService.insert(contactDisplay);
        }
        return "OK";
    }

    /**
     * 查询得到所有联系人信息
     *
     * @return
     */
    public ArrayList<ContactDisplay> getContacts() {
        ArrayList<ContactDisplay> contactDisplays = contactService.queryAll();
        return contactDisplays;
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
    public ArrayList<ContactDisplay> getContactByGroup(String groupnames) {
        ArrayList<ContactDisplay> contactDisplays = contactService.queryByGroup(groupnames);
        return contactDisplays;
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
                ContactDisplay contactDisplay = new ContactDisplay();
                contactDisplay.setGroupname(groupname);
                contactService.insert(contactDisplay);
            } else {// 有ids，对应更新
                String[] ids = idsString.split(",");
                for (String id : ids) {
                    ContactDisplay contactDisplay = contactService.queryById(Long.parseLong(id));
                    contactDisplay.setGroupname(groupname);
                    // 拿到操作模式
                    String addContactsType = p.get("addContactsType");
                    if (addContactsType.equals("copy")) {
                        contactDisplay.setId(0);//改id为0，就是等着新增
                        contactService.insert(contactDisplay);
                    } else if (addContactsType.equals("move")) {
                        contactService.update(contactDisplay);
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
    public ArrayList<ContactDisplay> getContactByIds(String ids) {
        ArrayList<ContactDisplay> contactDisplays = new ArrayList<ContactDisplay>();
        String[] idArray = ids.split(",");
        for (String s : idArray) {
            ContactDisplay contactDisplay = contactService.queryById(Long.parseLong(s));
            if (contactDisplay != null) {
                contactDisplays.add(contactDisplay);
            }
        }
        System.out.println("contactDisplays.size:" + contactDisplays.size());
        return contactDisplays;
    }


    // 将已经编辑好的联系人信息存储成Excel文件，并返回给调用者（MainController）用于下载
    public boolean initContactsFile(String filePath){
        // 1.拿到所有联系人信息
        ArrayList<ContactDisplay> contactDisplays = contactService.queryAll();
        if(contactDisplays == null || contactDisplays.size()==0){
            ContactDisplay contactDisplay = new ContactDisplay();
            contactDisplays.add(contactDisplay);
        }
        return ExcelUtil.initContactsFile(filePath, contactDisplays);
    }

}
