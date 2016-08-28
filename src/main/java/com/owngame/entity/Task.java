package com.owngame.entity;

import java.sql.Date;

/**
 * 当服务器生成一个需要使用短信发送的任务，就称之为一个Task
 * Created by Administrator on 2016/8/28.
 */
public class Task {
    String id;
    int state;
    String receivers;
    String content;
    Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
