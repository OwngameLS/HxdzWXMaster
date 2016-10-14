package com.owngame.entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 功能中关键字查询的结果
 * Created by Administrator on 2016-10-14.
 */
public class FunctionKeywordsResult {
    /* 逻辑设计：
    当查询某一组关键字去验证以前是否存在时，分别查询这一组中的每一个
    当查询均没有重复时，返回isSuccess=true;
    当查询到某一个关键字重复，告知，其结构如下：xxx关键字已存在，类似的关键字还有：xxx1,xxx2...
     */
    boolean isSuccess = true;// 验证是否成功
    ArrayList<String> simlarKeys;// 当查询关键字与其他关键字相似时填写


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ArrayList<String> getSimlarKeys() {
        return simlarKeys;
    }

    public void setSimlarKeys(ArrayList<String> simlarKeys) {
        this.simlarKeys = simlarKeys;
    }
}
