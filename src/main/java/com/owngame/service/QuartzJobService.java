package com.owngame.service;



import com.owngame.entity.Function;
import com.owngame.service.impl.AnswerServiceImpl;
import com.owngame.service.impl.ContactServiceImpl;
import com.owngame.service.impl.FunctionServiceImpl;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;

/**
 * QuartzJobService 主要执行定时调度业务的逻辑功能
 * Created by Administrator on 2016-8-30.
 */
@Service("mainService")
public class QuartzJobService implements Serializable {
    @Autowired
    AnswerService answerService;
    /**
     * 处理定时任务的查询
     *
     * @param triggerName
     * @param jobDataMap
     */
    public void handleTriggerAsk(String triggerName, JobDataMap jobDataMap) {
// 这里执行定时调度业务
        System.out.println("1动态执行了" + triggerName);
        /* jobDataMap中的信息
            functions:准备进行何种操作 此处是准备何种信息 names
            receivers:包含了什么信息 此处是准备传递给哪些人 ids
            description:这个定时任务的描述
        */
        String functions = ((String) jobDataMap.get("functions")).trim();
        String receiversIds = (String) jobDataMap.get("receivers");
        String description = (String) jobDataMap.get("description");
        answerService.handleAsk(functions,
                FunctionServiceImpl.QUESTIONTYPE_FUNCTION_NAME,
                receiversIds,
                ContactServiceImpl.CONTACT_TYPE_SUPERMAN,
                AnswerServiceImpl.ASK_TYPE_TRIGGERJOB,
                description);

//        String contents = "";
//        String name = "定时任务(";
//        // 查询所要结果
//        // 根据所涉及的function来处理
//        for (int i = 0; i < functions.length; i++) {
//            // 拿到function信息
//            Function function = functionService.queryByName(functions[i]);
//            name = name + functions[i];
//            contents = contents + functionService.getFunctionResult(function);
//        }
//        name = name + ")";
//        String receivers = "";// 因为上面得到的是ids，这里就查询成对应的手机号码吧
//        String receiversArr[] = receiversIds.split(",");
//        for (int i = 0; i < receiversArr.length; i++) {
//            receivers = receivers + contactService.queryById(Long.parseLong(receiversArr[i])).getPhone();
//            if (i + 1 < receiversArr.length) {
//                receivers = receivers + ",";
//            }
//        }
//        System.out.println("handleTriggerAsk contents:" + contents);
//        // 将结果组织成Task
//        taskService.createTask(name, description, contents, receivers);
    }

}
