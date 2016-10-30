package com.owngame.web;

import com.owngame.dao.ContactDao;
import com.owngame.entity.*;
import com.owngame.service.*;
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
import java.util.HashMap;
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
    PcontactService pcontactService;
    @Autowired
    AnswerService answerService;
    @Autowired
    TimerTaskService timerTaskService;
    @Autowired
    FunctionService functionService;
    @Autowired
    ContactDao contactDao;
    @Autowired
    TaskService taskService;


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
        // 交由线程池中的线程去处理具体逻辑
        WeiXinCoreRoute weiXinCoreRoute = new WeiXinCoreRoute(s);
        pool.execute(weiXinCoreRoute);
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
        if (file.getSize() <= 20) {
            return "<script>window.parent.uploadFailed('" + ExcelUtil.myEncode("请确认你是否上传了正确的文件。^_^") + "');</script>";
        }

        Object o = null;
        try {
            o = ExcelUtil.parseContent(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ss = o.toString();
        if (ss.startsWith("ERR")) {
            //上传失败，返回到前台，调用uploadFailed()这个方法
            return "<script>window.parent.uploadFailed('" + ss + "');</script>";
        } else {
            // 去存储Contacts
            String result = pcontactService.doPContacts(o);
            if (result.equals("OK")) {
                return "<script>window.parent.uploadSuccess();</script>";
            } else {
                return "<script>window.parent.uploadFailed('" + "再插入数据时出错。" + "');</script>";
            }
        }
    }


    /**
     * 下载文件
     *
     * @return
     * @throws IOException
     */
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

    /**
     * 返回试图（网页）
     *
     * @param view
     * @return
     */
    @RequestMapping(value = "/view/{view}", method = RequestMethod.GET)
    public String views(@PathVariable("view") String view) {
        return view;
    }


    /**
     * 根据组名返回该组的联系人信息
     *
     * @param groupname
     * @return
     */
    @RequestMapping(value = "/contacts/{groupname}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getContactByGroup(@PathVariable("groupname") String groupname) {
        System.out.println("getContactByGroup.......");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", pcontactService.getContactByGroup(groupname));
        return map;
    }

    /**
     * 返回所有的分组信息
     *
     * @return
     */
    @RequestMapping(value = "/contacts/groups", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getGroups() {
        System.out.println("getGroups.......");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groups", pcontactService.getGroups());
        return map;
    }

    /**
     * 更新某个联系人的信息
     *
     * @param contact
     * @return
     */
    @RequestMapping(value = "/contacts/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateContact(@RequestBody Contact contact) {
        System.out.println("contact:  " + contact.toString());
        if (contact.getId() > 0) {//是更新
            contactDao.update(contact);
        } else {
            System.out.println("insert contact!");
            contact.setId(0);
            contactDao.insert(contact);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("contacts", pcontactService.getContactByGroup(contact.getGroupname()));
        return map;
    }

    /**
     * 删除联系人
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/contacts/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteContact(@RequestBody Map<String, Long> p) {
        // 先查询这个id属于那个组
        String groupname = contactDao.queryById(p.get("id")).getGroupname();
        // 删除操作
        contactDao.delete(p.get("id"));
        Map<String, Object> map = new HashMap<String, Object>();
        // 返回更新后的该组信息
        map.put("contacts", pcontactService.getContactByGroup(groupname));
        return map;
    }


    /**
     * 通过姓名查询联系人（模糊查询）
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/contacts/searchbyname", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchContactsByName(@RequestBody Map<String, String> p) {
        String name = p.get("name");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", contactDao.queryLikeName("%" + name + "%"));
        return map;
    }

    /**
     * 通过ids查询联系人信息
     *
     * @param p
     * @return
     */
    @RequestMapping(value = "/contacts/searchbyids", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> searchContactsByIds(@RequestBody Map<String, String> p) {
        String ids = p.get("ids");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("contacts", pcontactService.getContactByIds(ids));
        return map;
    }


    /**
     * 对分组信息的增删改查
     *
     * @param p
     * @param action
     * @return
     */
    @RequestMapping(value = "/group/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleGroup(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        // 先判断操作
        if (action.equals("update")) {
            String ori = p.get("originalGroupName");
            String newName = p.get("groupname");
            contactDao.updateGroup(new GroupName(0, ori, newName));
        } else if (action.equals("delete")) {
            contactDao.deleteGroup(p.get("originalGroupName"));
        } else if (action.equals("insert")) {
            return pcontactService.insertGroup(p);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", "success");
        return map;
    }


    /**
     * 操作定时任务
     *
     * @param p
     * @param action
     * @return
     */
    @RequestMapping(value = "/timertask/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleTimerTask(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        // 先判断操作
        if (action.equals("update")) {
            long id = Long.parseLong(p.get("id"));
            TimerTask timerTask;
            if (id <= 0) {// 插入
                timerTask = new TimerTask();
            } else {
                timerTask = timerTaskService.queryById(id);
            }
            timerTask.setFunctions(p.get("functions"));
            timerTask.setReceivers(p.get("contacts"));
            timerTask.setFirerules(p.get("cron"));
            timerTask.setDescription(p.get("description"));
            timerTask.setState(p.get("state"));
            if (id <= 0) {// 插入
                timerTaskService.createTimerTask(timerTask);
            } else {
                timerTaskService.update(timerTask);
            }
        } else if (action.equals("delete")) {
            System.out.println("delete");
            long id = Long.parseLong(p.get("id"));
            timerTaskService.deleteById(id);
        }
        // 返回所有 用于刷新页面
        return queryTimerTasks();
    }

    /**
     * 查询所有定时任务
     *
     * @return
     */
    @RequestMapping(value = "/timertask/getall", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryTimerTasks() {
        System.out.println("queryTimerTasks...");
        ArrayList<TimerTask> timerTasks = timerTaskService.queryAll();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("timerTasks", timerTasks);
        return map;
    }

    /**
     * 查询所有功能
     *
     * @return
     */
    @RequestMapping(value = "/functions", method = RequestMethod.GET)
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
    @RequestMapping(value = "/functions/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryFunctionsById(@PathVariable("id") long id) {
        Function function = functionService.getById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("function", function);
        return map;
    }

    /**
     * 查询数据库设置连接的连通性
     *
     * @return
     */
    @RequestMapping(value = "/functions/testconnect", method = RequestMethod.POST)
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
    @RequestMapping(value = "/functions/keywords", method = RequestMethod.POST)
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
    @RequestMapping(value = "/functions/sql", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> checkSql(@RequestBody Map<String, String> p) {
        System.out.println("checkSql...");
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
    @RequestMapping(value = "/functions/{action}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleFunction(@RequestBody Map<String, String> p, @PathVariable("action") String action) {
        System.out.println("handleFunction action:" + action);
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
                int ret = functionService.createFunction(function);
            }
        } else if (action.equals("delete")) {
            System.out.println("delete....");
            functionService.deleteById(id);
        }
        // 查询所有功能
        return queryFunctions();
    }

    /**
     * 查询所有与手机端交互的任务
     *
     * @return
     * @Param lasthours 查询几个小时以内
     */
    @RequestMapping(value = "/tasks/{lasthours}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> showTasks(@PathVariable("lasthours") int lasthours) {
        ArrayList<Task> tasks = taskService.queryTasksBeforeTime(lasthours);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tasks", tasks);
        return map;
    }

}
