package com.owngame.entity;

/**
 * 功能对应表中的字段和其规则
 * a,-1,NN#b,5,BB#c,200,LL#d,abcd,NE@V
 // 字段，值，规则 根据规则来判断
 // 当规则为NN 表示没有规则不报警
 // 当规则为BB 表示大于值时报警
 // 当规则为BE 表示大于等于值时报警
 // 当规则为LL 表示小于值时报警
 // 当规则为LE 表示小于等于值时报警
 // 当规则为NE@V 表示不等于值(在V范围内)时报警
 *
 * Created by Administrator on 2016-10-10.
 */
public class FunctionFields {
    String fieldName;// 字段名
    String value;// 值
    String rule;// 规则

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

}
