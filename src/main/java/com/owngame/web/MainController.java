package com.owngame.web;

import com.owngame.dao.ContactDao;
import com.owngame.entity.Contact;
import com.owngame.service.AnswerService;
import com.owngame.service.CoreService;
import com.owngame.utils.CheckUtil;
import com.owngame.utils.ExcelUtil;
import com.owngame.utils.InfoFormatUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 从微信服务器转发过来的请求消息都从这里归总处理
 * Created by Administrator on 2016-8-17.
 */

@Controller
@RequestMapping("Smserver")
public class MainController {

    static ExecutorService pool;// 待处理的线程池

    // 线程池
    static {
        //创建一个可重用固定线程数的线程池
        pool = Executors.newFixedThreadPool(3);
    }

    @Autowired
    AnswerService answerService;
    @Autowired
    ContactDao contactDao;

    /**
     * 处理来自微信服务器的验证
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param writer
     */
    @RequestMapping(method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    public void validate(@RequestParam("signature") String signature,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("nonce") String nonce,
                         @RequestParam("echostr") String echostr,
                         Writer writer) {
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


    @RequestMapping(method = {RequestMethod.POST}, produces = "application/xml;charset=UTF-8")
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
     * 从手机端发送来询问服务器状态、是否有待处理任务，已经客户端发来的询问信息（想要获取某种信息）
     * <p>
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     *
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
     * 处理客户端提交任务处理信息，更新服务器端的数据
     * <p>
     * 返回json数据格式的方法
     * 因为开启了相应的配置，所以只要用上特定的@ResponseBody，它就能把返回的对象做成Json对象返回了。
     *
     * @return
     */
    @RequestMapping(value = "/commitTask/{id}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handleCommit(@PathVariable("id") long id, @PathVariable("state") int state) {
        return answerService.handleCommit(id, state);
    }

    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object doUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("file:" + file.getSize());
        if(file.getSize() <= 20 ){
            return "<script>window.parent.uploadFailed('"+ExcelUtil.myEncode("请确认你是否上传了正确的文件。^_^")+"');</script>";
        }

        Object o = null;
        try {
            o = ExcelUtil.parseContent(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ss = o.toString();
        ArrayList<Contact> contacts = null;
        if (ss.startsWith("ERR")) {
            //上传失败，返回到前台，调用uploadFailed()这个方法
            return "<script>window.parent.uploadFailed('" + ss + "');</script>";
        } else {
            // 去存储Contacts
            contacts = (ArrayList<Contact>) o;
            for (Contact contact : contacts) {
                contactDao.insert(contact);
                System.out.println(contact.toString());
            }
            return "<script>window.parent.uploadSuccess();</script>";
        }

    }


    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download() throws IOException {
//        String dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");
        String dfileName = "d:/contacts.xls";// 写死了这里
        File file = new File(dfileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }


}
