package com.owngame.entity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-20.
 * 筛选之后的功能结果
 */
public class FunctionFilterResult {
    String result = "";
    String functionNames;
    ArrayList<Function> functions;

    public FunctionFilterResult(String result, String functionNames, ArrayList<Function> functions) {
        this.result = result;
        this.functionNames = functionNames;
        this.functions = functions;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFunctionNames() {
        return functionNames;
    }

    public void setFunctionNames(String functionNames) {
        this.functionNames = functionNames;
    }

    public ArrayList<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<Function> functions) {
        this.functions = functions;
    }
}
