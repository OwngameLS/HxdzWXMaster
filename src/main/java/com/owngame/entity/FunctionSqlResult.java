package com.owngame.entity;

import java.util.ArrayList;

/**
 * 检查功能中Sql语句检查结果
 * Created by Administrator on 2016/10/15.
 */
public class FunctionSqlResult {
    int isSuccess = 1;// 验证是否成功 大于0就是成功了
    ArrayList<String> fields;// 分析得到的字段

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }
}
