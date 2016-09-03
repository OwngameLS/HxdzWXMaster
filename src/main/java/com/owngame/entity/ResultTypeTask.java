package com.owngame.entity;

import java.util.ArrayList;

/**
 * 返回请求的类型之一 Task
 * Created by Administrator on 2016-9-3.
 */
public class ResultTypeTask {
    String type;
    ArrayList<Task> tasks;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
