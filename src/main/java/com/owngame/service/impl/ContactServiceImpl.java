package com.owngame.service.impl;

import com.owngame.dao.ContactBaseDao;
import com.owngame.dao.ContactDao;
import com.owngame.dao.ContactHighDao;
import com.owngame.entity.*;
import com.owngame.service.ContactBaseService;
import com.owngame.service.ContactHighService;
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
public class ContactServiceImpl implements ContactService {

    @Autowired
    ContactBaseService contactBaseService;
    @Autowired
    ContactHighService contactHighService;
    @Autowired
    TaskService taskService;

    ContactBase contactBase;
    ContactHigh contactHigh;



    /**
     * 微信有关的用户绑定
     *
     * @param phone           手机号
     * @param openid          微信号
     * @param identifyingCode 验证码
     * @return
     */
    public int updateFromWeixin(String phone, String openid, String identifyingCode) {
        // 根据phone查找用户
        ContactHigh contactHigh = null;
        if(phone != null){
            contactHigh = contactHighService.queryByPhone(phone);
        }
        if (contactHigh != null) {
            // 如果找到了，说明是合法手机号用户，判断其openid
            String formerOpenid = contactHigh.getOpenid();
            if (formerOpenid != null) {// 原来绑定了微信
                if (openid.equals(formerOpenid)) {
                    // 未发生变化
                    return WeixinMessageServiceImpl.RETURN_CODE_SUCCESS;// 告知成功
                } else {// 发生了变化，用户换了一个微信来绑定已有的手机号
                    // 判断身份 发送一个验证码给已经绑定的手机
                    String randomCode = "" + System.currentTimeMillis();
                    randomCode = randomCode.substring(randomCode.length() - 6);// 后六位
                    // 将随机数存入contact 的backup字段，然后发送短信
                    contactHigh.setBackup(phone + "@@" + randomCode);// 用@@分隔此次申请的手机号
                    contactHighService.update(contactHigh);
                    sendMsgOfRandom(randomCode, phone);
                    return WeixinMessageServiceImpl.RETURN_CODE_CHANGE_OPENID;// 绑定的微信号发生变化
                }
            }
        } else {
            if(phone != null) {// 是带着手机号来查询的
                // 该电话号码没有绑定过
                // 告知用户 必须要先经过管理员添加权限
                return WeixinMessageServiceImpl.RETURN_CODE_NOT_AUTHORIZE_PHONE;
            }else{ // 没有带着手机号来 说明是改变绑定微信号的
                // 根据identifyingCode来查询
                contactHigh = contactHighService.queryByBackup("%" + identifyingCode + "%");
                if(contactHigh != null){// 能找到 说明验证码已经正确了
                    // 更新绑定的微信号
                    contactHigh.setBackup(null);
                    contactHigh.setOpenid(openid);
                    contactHighService.update(contactHigh);
                } else {// 未找到 验证码错误
                    return WeixinMessageServiceImpl.RETURN_CODE_INVALID_AUTHORIZECODE;
                }
            }
        }
        return WeixinMessageServiceImpl.RETURN_CODE_SUCCESS;// 告知成功
    }

    private int sendMsgOfRandom(String randomMsg, String phone) {
        Task task = new Task();
        task.setName("变更绑定微信号");
        task.setDescription("手机号" + phone + "变更绑定其微信号，用户验证信息发送。");
        task.setContent(randomMsg);
        task.setReceivers(phone);
        task.setState(Task.STATE_WAITING);
        task.setCreateTime(new Date(System.currentTimeMillis()));
        // 插入数据库
        return taskService.insert(task);
    }



    public ContactDisplay queryById(long id) {
        // 1.先查询到ContactBase
        ContactBase contactBase = contactBaseService.queryById(id);
        ContactHigh contactHigh = null;
        // 2.再查询ContactHigh
        if(contactBase != null){
            contactHigh = contactHighService.queryById(contactBase.getHighid());
        }
        return new ContactDisplay(contactBase,contactHigh);
    }

    public ArrayList<ContactDisplay> queryDisplayByOpenId(String openid) {
        ContactBase contactBase = null;
        // 1.根据openid找到highid
        ContactHigh contactHigh = queryHighByOpenId(openid);
        // 根据high查询得到Display
        return getDisplayByHigh(contactHigh);
    }

    public ContactHigh queryHighByOpenId(String openid) {
        return contactHighService.queryByOpenId(openid);
    }

    public ArrayList<ContactDisplay> queryDisplayByPhone(String phone) {
        ContactBase contactBase = null;
        // 1.根据phone找到highid
        ContactHigh contactHigh = queryHighByOpenId(phone);
        // 根据high查询得到Display
        return getDisplayByHigh(contactHigh);
    }

    public ContactHigh queryHighByPhone(String phone) {
        return contactHighService.queryByPhone(phone);
    }

    public ArrayList<ContactDisplay> queryDisplayByBackup(String backup){
        ContactBase contactBase = null;
        // 1.根据backup找到highid
        ContactHigh contactHigh = queryHighByBackup(backup);
        // 根据high查询得到Display
        return getDisplayByHigh(contactHigh);
    }

    public ContactHigh queryHighByBackup(String backup){
        return contactHighService.queryByBackup(backup);
    }


    public ArrayList<ContactDisplay> queryLikeName(String name) {
        ArrayList<ContactBase> contactBases = contactBaseService.queryLikeName(name);
        return getDisplaysByBases(contactBases);
    }

    // 通过联系人所在组找到联系人
    public ArrayList<ContactDisplay> queryByGroup(String groupname) {
        ArrayList<ContactBase> contactBases = contactBaseService.queryByGroup(groupname);
        return getDisplaysByBases(contactBases);
    }

    public ArrayList<ContactDisplay> queryAll() {
        ArrayList<ContactBase> contactBases =  contactBaseService.queryAll();
        return getDisplaysByBases(contactBases);
    }

    public ArrayList<String> getGroups() {
        return contactBaseService.getGroups();
    }

    public int insert(ContactDisplay contactDisplay) {
        // 1.先整理成对应的信息
        initBaseAndHigh(contactDisplay);
        // 2.插入高级信息
        int r = contactHighService.insert(contactHigh);
        if(r < 0){
            return r;
        }
        contactBase.setHighid(r);
        // 3.插入基本信息
        return contactBaseService.insert(contactBase);
    }

    public int update(ContactDisplay contactDisplay) {
        // 1.先整理成对应的信息
        initBaseAndHigh(contactDisplay);
        // 2.更新高级信息
        int r = contactHighService.update(contactHigh);
        if(r < 0){// 数据库错误
            return r;
        } else if(r > 1){// 虽然是更新，但是是插入操作
            contactBase.setHighid(r);
        }
        // 3.更新基本信息
        return contactBaseService.update(contactBase);
    }

    public int updateGroup(GroupName groupName) {
        return contactBaseService.updateGroup(groupName);
    }

    public int updateGroupWithId(GroupName groupName) {
        return contactBaseService.updateGroupWithId(groupName);
    }

    public int delete(long id) {
        // 1.通过id获得high
        ContactBase contactBase = contactBaseService.queryById(id);
        long high_id = contactBase.getHighid();
        ContactHigh contactHigh = contactHighService.queryById(contactBase.getHighid());
        // 2.先删除这个base
        contactBaseService.delete(id);
        return deleteNoRelyHigh(high_id);
    }

    public int deleteGroup(String groupname) {
        // 1.先查看该组的信息
        ArrayList<ContactBase> contactBases = contactBaseService.queryByGroup(groupname);
        int r = contactBaseService.deleteGroup(groupname);
        if(r<0){
            return r;
        }else{
            // 2.检查这些被删掉的人员信息中 highid是否还有依赖
            if(contactBases != null){
                for (int i=0;i<contactBases.size();i++){
                    r = deleteNoRelyHigh(contactBases.get(i).getHighid());
                    if(r<0){
                        return  r;
                    }
                }
            }
        }
        return 0;
    }

    public int deleteAll() {
        // 调用此方法仅仅出现在批量导入时，所以只删除base信息表
        return contactBaseService.deleteAll();
    }

    // 删除不在被依赖的High信息
    private int deleteNoRelyHigh(long high_id){
        if(high_id != -1){
            // 如果对应的high_id还有其他base与之关联，就不删除
            ArrayList<ContactBase> contactBases = contactBaseService.queryByHighId(high_id);
            if(contactBases != null){
                if(contactBases.size() == 0){
                    // 未查询到 删除
                    return contactHighService.delete(high_id);
                }
            }else{
                // 未查询到 删除
                return contactHighService.delete(high_id);
            }
        }
        return 0;
    }


    // 通过high信息查找补齐bases， 得到displays
    private ArrayList<ContactDisplay> getDisplayByHigh(ContactHigh contactHigh){
        ArrayList<ContactDisplay> contactDisplays = null;
        ArrayList<ContactBase> contactBases = null;
        if(contactHigh != null){
            // 查询对应的所有Bases
            contactBases = contactBaseService.queryByHighId(contactHigh.getId());
            if(contactBases != null){
                contactDisplays = new ArrayList<ContactDisplay>();
                for(ContactBase contactBase: contactBases){
                    contactDisplays.add(new ContactDisplay(contactBase, contactHigh));
                }
            }
            return contactDisplays;
        }else{
            return null;
        }
    }

    // 通过Bases 补齐查找highs 得到Displays
    private ArrayList<ContactDisplay> getDisplaysByBases(ArrayList<ContactBase> contactBases){
        // 将已经查到的高级信息存储起来，方便下次查找，这样有利于降低数据库压力
        ArrayList<ContactHigh> contactHighs = new ArrayList<ContactHigh>();
        ArrayList<ContactDisplay> contactDisplays = new ArrayList<ContactDisplay>();
        for(int i=0;i<contactBases.size();i++){
            ContactHigh contactHigh = queryInArrays(contactBases.get(i).getHighid(), contactHighs);
            if(contactHigh == null){// 在缓存中未找到，就去查询数据库
                contactHigh = contactHighService.queryById(contactBases.get(i).getHighid());
            }
            contactDisplays.add(new ContactDisplay(contactBases.get(i), contactHigh));
        }
        return contactDisplays;
    }

    // 在已经查找到的High信息队列中查找highid对应的信息
    private ContactHigh queryInArrays(long highid, ArrayList<ContactHigh> contactHighs){
        for(int i=0;i<contactHighs.size();i++){
            if(highid == contactHighs.get(i).getId()){
                return contactHighs.get(i);
            }
        }
        return null;
    }

    // 用contactDisplay 初始化 两个组成对象
    private void initBaseAndHigh(ContactDisplay contactDisplay){
        contactBase = new ContactBase();
        contactBase.setId(contactDisplay.getBase_id());
        contactBase.setGroupname(contactDisplay.getGroupname());
        contactBase.setDescription(contactDisplay.getDescription());
        contactBase.setName(contactDisplay.getName());
        contactBase.setTitle(contactDisplay.getTitle());
        contactHigh = new ContactHigh();
        contactHigh.setId(contactDisplay.getHigh_id());
        contactHigh.setPhone(contactDisplay.getPhone());
        contactHigh.setGrade(contactDisplay.getGrade());
        contactHigh.setOpenid(contactDisplay.getOpenid());
        contactHigh.setBackup(contactDisplay.getBackup());
    }
}
