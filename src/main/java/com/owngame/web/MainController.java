package com.owngame.web;

import com.owngame.service.ContactService;
import com.owngame.service.WeixinService;
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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * 从微信服务器转发过来的请求消息都从这里归总处理
 * Created by Administrator on 2016-8-17.
 */

@Controller
@RequestMapping("Smserver")
public class MainController {
    @Autowired
    ServletContext context;// 获得环境变量的入口！
    @Autowired
    ContactService contactService;
    @Autowired
    WeixinService weixinService;

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


    /**
     * 处理来自微信服务器转发的微信消息事件
     *
     * @param request
     * @param response
     * @throws IOException
     */
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
        // 交由微信服务去处理具体逻辑
        weixinService.handleRawMessage(s);
    }

    /**
     * 处理上传通讯录文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object doUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("file:" + file.getSize());
        if (file.getSize() <= 20) {// 文件过小 怀疑正确性
            return "<script>window.parent.uploadFailed('" + ExcelUtil.myEncode("请确认你是否上传了正确的文件。^_^") + "');</script>";
        }

        Object o = null;
        try {
            o = ExcelUtil.parseContent(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ss = o.toString();
//        System.out.println("ss:" + ss);
        if (ss.startsWith("ERR")) {
            //上传失败，返回到前台，调用uploadFailed()这个方法
            return "<script>window.parent.uploadFailed('" + ss + "');</script>";
        } else {
            // 去存储Contacts
            String result = contactService.batchUpdateContacts(o);
            if (result.equals("OK")) {
                return "<script>window.parent.uploadSuccess();</script>";
            } else {
                return "<script>window.parent.uploadFailed('" + "在插入数据时出错。" + "');</script>";
            }
        }
    }


    /**
     * 下载文件通讯录文件
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download/{resourcename}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable("resourcename") String resourcename) throws IOException {
        String dfileName = context.getRealPath("/");
        if (resourcename.equals("contacts")) {
            dfileName += "contacts/contacts.xls";// 获得文件路径的方法
            // 调用联系人方法，获得返回的联系人文件
            contactService.initContactsFile(dfileName);
        } else if (resourcename.equals("app")) {
            dfileName += "download/app-release.apk";// 获得文件路径的方法
        } else if (resourcename.equals("guidebook")) {
            dfileName += "download/guide.pdf";// 获得文件路径的方法
        }
        File file = new File(dfileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", dfileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);

    }

    /**
     * 返回视图（网页）
     *
     * @param view
     * @return
     */
    @RequestMapping(value = "/view/{view}", method = RequestMethod.GET)
    public String views(@PathVariable("view") String view) {
        return view;
    }


}
