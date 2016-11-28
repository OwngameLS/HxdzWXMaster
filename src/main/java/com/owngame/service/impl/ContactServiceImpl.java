package com.owngame.service.impl;

import com.owngame.dao.ContactDao;
import com.owngame.entity.Contact;
import com.owngame.entity.GroupName;
import com.owngame.entity.Task;
import com.owngame.service.ContactService;
import com.owngame.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016-11-28.
 */
@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    ContactDao contactDao;
    @Autowired
    TaskService taskService;

    public int deleteById(long id) {
        return contactDao.delete(id);
    }

    public int updateFromWeixin(String phone, String openid) {
        // 根据phone查找用户
        Contact contact = contactDao.queryByPhone(phone);
        if(contact != null){
            // 如果找到了，说明是合法手机号用户，判断其openid
            String formerOpenid = contact.getOpenid();
            if(formerOpenid != null){// 原来绑定了微信
                if(openid.equals(formerOpenid)){
                    // 未发生变化
                    return 1;// 告知成功
                }else{// 发生了变化，用户换了一个微信来绑定已有的手机号
                    // 判断身份 发送一个验证码给已经绑定的手机
                    String randomCode = ""+System.currentTimeMillis();
                    randomCode = randomCode.substring(randomCode.length()-6);// 后六位
                    // 将随机数存入contact 的backup字段，然后发送短信
                    contact.setBackup(randomCode);
                    contactDao.update(contact);
                    sendMsgOfRandom(randomCode, phone);
                    return -2;
                }
            }
        } else {// 该电话号码没有绑定过
            // 告知用户 必须要先经过管理员添加权限
            return -999;
        }

        return 0;
    }

    private int sendMsgOfRandom(String randomMsg, String phone){
        Task task = new Task();
        task.setName("变更绑定微信号");
        task.setDescription("手机号"+phone+"变更绑定其微信号，用户验证信息发送。");
        task.setContent(randomMsg);
        task.setReceivers(phone);
        task.setState(Task.STATE_WAITING);
        task.setCreateTime(new Date(System.currentTimeMillis()));
        // 插入数据库
        return taskService.insert(task);
    }

    public Contact queryById(long id) {
        return contactDao.queryById(id);
    }

    public Contact queryByOpenId(String openid) {
        return contactDao.queryByOpenId(openid);
    }

    public Contact queryByPhone(String phone) {
        return contactDao.queryByPhone(phone);
    }

    public ArrayList<Contact> queryLikeName(String name) {
        return contactDao.queryLikeName(name);
    }

    public ArrayList<Contact> queryByGroup(String groupname) {
        return queryByGroup(groupname);
    }

    public ArrayList<Contact> queryAll() {
        return contactDao.queryAll();
    }

    public ArrayList<String> getGroups() {
        return contactDao.getGroups();
    }

    public int insert(Contact contact) {
        return contactDao.insert(contact);
    }

    public int update(Contact contact) {
        return contactDao.update(contact);
    }

    public int updateGroup(GroupName groupName) {
        return contactDao.updateGroup(groupName);
    }

    public int updateGroupWithId(GroupName groupName) {
        return contactDao.updateGroupWithId(groupName);
    }

    public int delete(long id) {
        return contactDao.delete(id);
    }

    public int deleteGroup(String groupname) {
        return contactDao.deleteGroup(groupname);
    }

    public int deleteAll() {
        return contactDao.deleteAll();
    }
}
