package com.owngame.entity;

/**
 * 功能中关键字查询的结果
 * Created by Administrator on 2016-10-14.
 */
public class KeywordsResult {
    /* 逻辑设计：
    当查询某一组关键字去验证以前是否存在时，分别查询这一组中的每一个
    当查询均没有重复时，返回isSuccess=true;
    当查询到某一个关键字重复，告知，其结构如下：xxx关键字已存在，类似的关键字还有：xxx1,xxx2...
     */
    int isSuccess = 1;// 验证是否成功 大于0成功 小于0失败
    String similarKeys = "";// 当查询关键字与其他关键字相似时填写

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getSimilarKeys() {
        return similarKeys;
    }

    public void setSimilarKeys(String similarKeys) {
        this.similarKeys = similarKeys;
    }
}
