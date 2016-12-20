package com.owngame.entity;

/**
 * 用来管理定时任务的实体类
 * <p>
 * Created by Administrator on 2016-9-19.
 */
public class TimerTask {
    long id;
    String name;// quartz表中的名字
    String functions;//任务功能（涉及的功能集合）
    String description;// 描述
    String firerules;//触发规则（cron字符串）
    String receivers;// 接收者们（id的组合）
    int receivetype;// 接收方式（sms 0, wx 1, smsandwx 2）
    String state;// 状态 是否启用

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirerules() {
        return firerules;
    }

    public void setFirerules(String firerules) {
        this.firerules = firerules;
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public int getReceivetype() {
        return receivetype;
    }

    public void setReceivetype(int receivetype) {
        this.receivetype = receivetype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TimerTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", functions='" + functions + '\'' +
                ", description='" + description + '\'' +
                ", firerules='" + firerules + '\'' +
                ", receivers='" + receivers + '\'' +
                ", receivetype=" + receivetype +
                ", state='" + state + '\'' +
                '}';
    }
}
