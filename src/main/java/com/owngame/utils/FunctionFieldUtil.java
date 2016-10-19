package com.owngame.utils;

import com.owngame.entity.FieldAndSelfName;
import com.owngame.entity.FunctionFieldRule;

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
    public static ArrayList<FunctionFieldRule> parseFieldsString(String fields){
        // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,NE@V
        String f[] = fields.split("#");
        ArrayList<FunctionFieldRule> functionFields = new ArrayList<FunctionFieldRule>();
        for(int i=0;i<f.length;i++){
            FunctionFieldRule functionField = new FunctionFieldRule();
            String ff[] = f[i].split(",");
            functionField.setField(ff[0]);
            functionField.setFieldName(ff[1]);
            functionField.setCompareValue(ff[2]);
            functionField.setRule(ff[3]);
            functionFields.add(functionField);
        }
        return functionFields;
    }

    /**
     * 解析字段和其自定义名称的字符串
     * a,aName#b,bName#c,cName
     * @param fieldandSelfaNamesStr
     * @return
     */
    public static ArrayList<FieldAndSelfName> parseFieldSelfName(String fieldandSelfaNamesStr){
        String f[] = fieldandSelfaNamesStr.split("#");
        ArrayList<FieldAndSelfName> fieldAndSelfNames = new ArrayList<FieldAndSelfName>();
        for(int i=0;i<f.length;i++){
            FieldAndSelfName fieldAndSelfName = new FieldAndSelfName();
            String ff[] = f[i].split(",");
            fieldAndSelfName.setField(ff[0]);
            fieldAndSelfName.setSelfName(ff[1]);
            fieldAndSelfNames.add(fieldAndSelfName);
        }
        return fieldAndSelfNames;
    }
}
