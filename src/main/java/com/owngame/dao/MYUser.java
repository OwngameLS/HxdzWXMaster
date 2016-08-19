package com.owngame.dao;

import weixin.popular.bean.user.User;

/**
 * Created by Administrator on 2016-8-18.
 */
public class MYUser {
    private String unionid = null; // 多个公众号之间用户帐号互通UnionID机制
    private String openid = null; // 用户的标识，对当前公众号唯一
    private String nickname = null;
    private Integer sex = 0; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String city = null;
    private String province = null;
    private int grade = -1;// 等级
    private float guarantee = -1f;// 保证金
    private float wallet = -1f;// 钱包余额
    private String phonenumber = null;// 手机号码
    private String realname = null;// 真实姓名

    public MYUser(User u){
        this.unionid = u.getUnionid();
        this.openid = u.getOpenid();
        this.nickname = u.getNickname();
        this.sex = u.getSex();
        this.city = u.getCity();
        this.province = u.getProvince();
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public float getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(float guarantee) {
        this.guarantee = guarantee;
    }

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
