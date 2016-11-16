package com.owngame.web;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.Function;
import com.owngame.entity.FunctionKeywordsResult;
import com.owngame.entity.FunctionSqlResult;
import com.owngame.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-7.
 */

@Controller
@RequestMapping("Smserver/functions")
public class FunctionController {

    @Autowired
    FunctionService functionService;

    @Autowired
    FunctionDao functionDao;

    /**
     * 获得多个方法的结果
     *
     * @return
     */
    @RequestMapping(value = "/getresults", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getFunctionResults(@RequestBody Map<String, String> p) {
        String ids[] = p.get("ids").split(",");
        String results = "";
        for (int i = 0; i < ids.length; i++) {
            Function function = functionDao.queryById(Long.parseLong(ids[i]));
            results = results + functionService.getFunctionResult(function) + ";";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("results", results);
        return map;
    }

    /**
     * 通过关键字获得方法的查询结果
     * @param p
     * @return
     */
    @RequestMapping(value = "/getresults/keywords", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getFunctionResultsByKeywords(@RequestBody Map<String, String> p){
        String keywords = p.get("keywords");
        String results = "";
        if(keywords != null){
            if(keywords.equals("") == false){
                // 1.先将关键字分组
                keywords.replaceAll("，", ",");// 将中文逗号替换为英文
                String[] keyStr = keywords.split(",");
                Map<String, String> idsMap = new HashMap<String, String>();
                ArrayList<String> ids = new ArrayList<String>();
                // 2.根据关键字查询方法
                for(int i=0;i<keyStr.length;i++){
                    Function function = functionService.getByKeywords(keyStr[i]);
                    if(function != null){
                        if(function.getId() != -1){// 找到了这个方法
                            ids = addIdsUnique(ids, function.getId()+"");// 去重添加
                        }else{// 找到了类似关键字的方法
                            results = results + "关键字" + keyStr[i] + " 没有找到对应的功能，因此没有获得查询结果，与它类似的关键字有 "
                                    + function.getDescription() +",请与管理员确认后再次尝试查询。";
                        }
                    }
                }
                // 3.根据获得的方法查询其对应的结果
                if(ids.size() != 0){
                    // 确实查询到了方法
                    String s = "";
                    for(int i=0;i<ids.size();i++){
                        s = s + ids.get(i);
                        if((i+1)<ids.size()){
                            s = s + ",";
                        }
                    }
                    idsMap.put("ids", s);
                    Map<String, Object> res = getFunctionResults(idsMap);
                    results = results + (String) res.get("results");
                }else{
                    results = results + "您所查询的所有关键字均未找到对应的功能，请使用正确的关键字查询，如果你不清楚请联系系统管理员。";
                }
            }else{
                results = "请使用正确的关键字查询，如果你不清楚请联系系统管理员。";
            }
        }else{
            results = "请使用正确的关键字查询，如果你不清楚请联系系统管理员。";
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("results", results);
        return map;
    }

    // 保证每次添加的方法都是唯一的
    private ArrayList<String> addIdsUnique(ArrayList<String> ids, String id) {
        boolean isFound = false;
        for(int i=0;i<ids.size();i++){
            if(id.equals(ids.get(i))){
                isFound = true;
                break;
            }
        }
        if(isFound == false){
            ids.add(id);
        }
        return ids;
    }


    /**
     * 查询所有功能
     *
     * @return
     */
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryFunctions() {
        ArrayList<Function> functions = functionService.queryAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("functions", functions);
        return map;
    }

    /**
     * 通过id查询功能
     *
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryFunctionsById(@PathVariable("id") long id) {
        Function function = null;
        if (id == -1) {
            function = new Function();
        } else {
            function = functionService.getById(id);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("function", function);
        return map;
    }

    /**
     * 查询数据库设置连接的连通性
     *
     * @return
     */
    @RequestMapping(value = "/testconnect", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> testFunctionConnect(@RequestBody Map<String, String> p) {
        Function function = new Function();
        function.setIp(p.get("ip"));
        function.setPort(p.get("port"));
        function.setDbtype(p.get("dbtype"));
        function.setDbname(p.get("dbname"));
        function.setUsername(p.get("username"));
        function.setPassword(p.get("password"));
        function.setTablename(p.get("tablename"));
        // 检查连通性
        ArrayList<String> colNames = functionService.testConnect(function);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("colNames", colNames);
        return map;
    }

    /**
     * 查询关键字
     *
     * @return
     */
    @RequestMapping(value = "/keywords", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkKeywords(@RequestBody Map<String, String> p) {
        String idString = p.get("id");
        String keywords = p.get("keywords");
        long id = Long.parseLong(idString);
        // 检查关键字
        FunctionKeywordsResult keywordResult = functionService.checkKeywords(id, keywords);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keywordResult", keywordResult);
        return map;
    }

    /**
     * 检查Sql语句
     *
     * @return
     */
    @RequestMapping(value = "/sql", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkSql(@RequestBody Map<String, String> p) {
        Function function = new Function();
        function.setIp(p.get("ip"));
        function.setPort(p.get("port"));
        function.setDbtype(p.get("dbtype"));
        function.setDbname(p.get("dbname"));
        function.setUsername(p.get("username"));
        function.setPassword(p.get("password"));
        function.setTablename(p.get("tablename"));
        String sqlstmt = p.get("sqlstmt");
        // 检查Sql语句
        FunctionSqlResult sqlResult = functionService.checkSql(function, sqlstmt);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sqlResult", sqlResult);
        return map;
    }

    /**
     * 操作function 增、改、删
     *
     * @return
     */
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleFunction(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        long id = Long.parseLong(p.get("id"));
        if (action.equals("update")) {
            Function function = new Function();
            function.setName(p.get("name"));
            function.setDescription(p.get("description"));
            function.setKeywords(p.get("keywords"));
            function.setIp(p.get("ip"));
            function.setPort(p.get("port"));
            function.setDbtype(p.get("dbtype"));
            function.setDbname(p.get("dbname"));
            function.setUsername(p.get("username"));
            function.setPassword(p.get("password"));
            function.setTablename(p.get("tablename"));
            function.setUsetype(p.get("usetype"));
            function.setReadfields(p.get("readfields"));
            function.setSortfields(p.get("sortfields"));
            function.setFieldrules(p.get("fieldrules"));
            function.setIsreturn(p.get("isreturn"));
            function.setSqlstmt(p.get("sqlstmt"));
            function.setSqlfields(p.get("sqlfields"));
            function.setUsable(p.get("usable"));
            if (id > 0) {// 更新
                function.setId(id);
                functionService.update(function);
            } else {
                function.setId(0);
                int ret = functionService.createFunction(function);
            }
        } else if (action.equals("delete")) {
            functionService.deleteById(id);
        }
        // 查询所有功能
        return queryFunctions();
    }


}
