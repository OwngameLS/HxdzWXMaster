package com.owngame.entity;

import java.util.Date;

/**
 * 当服务器生成一个需要使用短信发送的任务，就称之为一个Task
 * Created by Administrator on 2016/8/28.
 */
public class Task {
    public static final int STATE_WAITING = 0;//刚创建
    public static final int STATE_HANDLING = 1;//正在处理
    public static final int STATE_DONE = 2;//处理完成

    long id;
    int state;
    String receivers;
    String content;
    Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
