package com.owngame.utils;

import com.owngame.entity.FunctionFields;

import java.util.ArrayList;

/**
 * 处理功能中字段的工具类
 * Created by Administrator on 2016-10-10.
 */
public class FunctionFieldUtil {
    /**
     * 解析fieldsString
     * @param fields
     * @return
     */
    public static ArrayList<FunctionFields> parseFieldsString(String fields){
        // a,-1,NN#b,5,BB#c,200,LL#d,abcd,NE@V
        String f[] = fields.split("#");
        ArrayList<FunctionFields> functionFieldses = new ArrayList<FunctionFields>();
        for(int i=0;i<f.length;i++){
            FunctionFields functionFields = new FunctionFields();
            String ff[] = f[i].split(",");
            functionFields.setFieldName(ff[0]);
            functionFields.setValue(ff[1]);
            functionFields.setRule(ff[2]);
            functionFieldses.add(functionFields);
        }
        return functionFieldses;
    }
}
