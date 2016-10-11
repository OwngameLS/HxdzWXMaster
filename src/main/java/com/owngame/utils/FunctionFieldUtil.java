package com.owngame.utils;

import com.owngame.entity.FunctionField;

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
    public static ArrayList<FunctionField> parseFieldsString(String fields){
        // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,NE@V
        String f[] = fields.split("#");
        ArrayList<FunctionField> functionFields = new ArrayList<FunctionField>();
        for(int i=0;i<f.length;i++){
            FunctionField functionField = new FunctionField();
            String ff[] = f[i].split(",");
            functionField.setField(ff[0]);
            functionField.setFieldName(ff[1]);
            functionField.setCompareValue(ff[2]);
            functionField.setRule(ff[3]);
            functionFields.add(functionField);
        }
        return functionFields;
    }
}
