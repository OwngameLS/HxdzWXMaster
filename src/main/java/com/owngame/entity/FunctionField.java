package com.owngame.entity;

/**
 * 功能对应表中的字段和其规则
 // a,aName,-1,NN#b,bName,5,BB#c,cName,200,LL#d,dName,abcd,NE@V
 // 字段，值，规则 根据规则来判断
 // 当规则为NN 表示没有规则不报警
 // 当规则为BB 表示大于值时报警
 // 当规则为BE 表示大于等于值时报警
 // 当规则为LL 表示小于值时报警
 // 当规则为LE 表示小于等于值时报警
 // 当规则为NE@V 表示不等于值(在V范围内)时报警
 // 当规则为EQ 表示等于给定值时报警（比如查询是否有报警）
 *
 * Created by Administrator on 2016-10-10.
 */
public class FunctionField {
    String field;// 字段（在表中的名字）
    String fieldName;//字段名（用于理解）
    String compareValue;// 值
    String rule;// 规则

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(String compareValue) {
        this.compareValue = compareValue;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

}
