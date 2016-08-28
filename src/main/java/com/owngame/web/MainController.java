package com.owngame.web;

import com.alibaba.fastjson.util.IOUtils;
import com.owngame.service.AnswerService;
import com.owngame.service.CoreService;
import com.owngame.utils.CheckUtil;
import com.owngame.utils.InfoFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.io.IOException;
import java.io.Writer;

/**
 * 从微信服务器转发过来的请求消息都从这里归总处理
 * Created by Administrator on 2016-8-17.
 */

@Controller
@RequestMapping("Smserver")
public class MainController {

    @Autowired
    AnswerService answerService;

    static ExecutorService pool;// 待处理的线程池
    // 线程池
    static{
        //创建一个可重用固定线程数的线程池
        pool = Executors.newFixedThreadPool(3);
    }

    /**
     * 处理来自微信服务器的验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param writer
     */
    @RequestMapping(method = { RequestMethod.GET }, produces = "application/json;charset=UTF-8")
    public void validate(@RequestParam("signature") String signature,
                   @RequestParam("timestamp") String timestamp,
                   @RequestParam("nonce") String nonce,
                   @RequestParam("echostr") String echostr,
                   Writer writer){
        System.out.println("fuck you !" + signature + ";" + timestamp + ";" + nonce + ";" + echostr);
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            // 验证成功，原样返回
            try {
                writer.write(echostr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequestMapping(method = { RequestMethod.POST }, produces = "application/xml;charset=UTF-8")
    public void post(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("handle post...");
        /* 消息的接收、处理、响应 */

        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        String s = InfoFormatUtil.inputStream2String(request.getInputStream());//在这里就要转换成String了，不知道为什么要这样
        response.setCharacterEncoding("UTF-8");
        // 响应消息
        PrintWriter out = response.getWriter();
        out.print("");// 先回应空消息 然后再用客服消息接口回复具体内容，避免等待
        out.close();
        // 交由线程池中的线程去处理具体逻辑
        CoreService coreService = new CoreService(s);
        pool.execute(coreService);
    }

    /**
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     * @return
     */
    @RequestMapping(value = "/askServer/{actionName}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleAsk(@PathVariable("actionName") String actionName) {
        // TODO 根据actionName来决定查询什么
        return answerService.handleAsk(actionName);
    }
    //
    /**
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     * @return
     */
    @RequestMapping(value = "commitTask/{id}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleCommit(@PathVariable("id") String id,@PathVariable("state") String state) {
        // TODO
        Map<String, Object> map = new HashMap<String, Object>();

        return map;
    }



}
