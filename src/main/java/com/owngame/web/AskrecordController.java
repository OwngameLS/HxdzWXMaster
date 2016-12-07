package com.owngame.web;

import com.owngame.entity.Askrecord;
import com.owngame.service.AskrecordService;
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
@RequestMapping("Smserver/askrecords")
public class AskrecordController {

    @Autowired
    AskrecordService askrecordService;

    /**
     * 查询记录
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> showAskrecords(@RequestBody Map<String, String> p) {
            int lasthours = Integer.parseInt(p.get("lasthours"));
            int type = Integer.parseInt(p.get("type"));
            String askers = p.get("askers");
            String functions = p.get("functions");
            int issuccess = Integer.parseInt(p.get("issuccess"));
            ArrayList<Askrecord> askrecords = askrecordService.handleQuery(lasthours, type, askers, functions, issuccess);
            System.out.println("size:" + askrecords.size());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("askrecords", askrecords);
            return map;
    }


}
