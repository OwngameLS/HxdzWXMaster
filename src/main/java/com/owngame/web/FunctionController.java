package com.owngame.web;

import com.owngame.entity.ContactHigh;
import com.owngame.entity.Function;
import com.owngame.entity.FunctionKeywordsResult;
import com.owngame.entity.FunctionSqlResult;
import com.owngame.service.ContactService;
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
    ContactService contactService;

    /**
     * 获得多个方法的结果
     *
     * @return
     */
    @RequestMapping(value = "/getresults", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getFunctionResults(@RequestBody Map<String, String> p) {
        Map<String, Object> map = new HashMap<String, Object>();
        String results = functionService.getFunctionResultByIds(p.get("ids"));
        map.put("results", results);
        return map;
    }

    /**
     * 通过关键字获得方法的查询结果
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/getresults/keywords", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getFunctionResultsByKeywords(@RequestBody Map<String, String> p) {
        Map<String, Object> map = new HashMap<String, Object>();
        ContactHigh contactHigh = contactService.queryHighByPhone("superman");
        String results = functionService.getFunctionResultsByKeywords(contactHigh, 2, p.get("keywords"));// 从网页上询问结果，最高级别
        map.put("results", results);
        return map;
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
            function.setGrade(p.get("grade"));
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
