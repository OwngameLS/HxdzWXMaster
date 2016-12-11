package com.owngame.service.impl;

import com.owngame.dao.AskrecordDao;
import com.owngame.entity.Askrecord;
import com.owngame.service.AskrecordService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Administrator on 2016-12-7.
 */
@Service
public class AskrecordServiceImpl implements AskrecordService {
    @Autowired
    AskrecordDao askrecordDao;

    /**
     * 处理前台查询 自己判断条件而不用数据库操作
     * 仅用时间条件作用于结果集
     *
     * @param lasthours
     * @param type
     * @param askers
     * @param functions
     * @param issuccess
     * @return
     */
    public ArrayList<Askrecord> handleQuery(int lasthours, int type, String askers, String functions, int issuccess) {
        ArrayList<Askrecord> askrecords = queryRecordsBeforeTime(lasthours);
        System.out.println("before size:" + askrecords.size());
        if (askrecords == null || askrecords.size() == 0) {
            return null;
        }

        ArrayList<Askrecord> askrecords2 = new ArrayList<Askrecord>();
        askers = askers.replaceAll("，", ",");
        String askersArray[] = askers.split(",");
        functions = functions.replaceAll("，", ",");
        String functionsArray[] = functions.split(",");
        for (int i = 0; i < askrecords.size(); i++) {
            Askrecord askrecord = askrecords.get(i);
            if (type != -1) {// 不是全部访问类型
                if (askrecord.getType() != type) {
                    continue;
                }
            }
            if (askers.equals("all") == false) {// 不是查询全部人员
                // 看是否包含
                if (isContain(askersArray, askrecord.getName()) == false) {
                    continue;
                }
            }
            if (functions.equals("all") == false) {// 不是查询全部人员
                // 看是否包含
                if (isContain(functionsArray, askrecord.getFunctions()) == false) {
                    continue;
                }
            }
            if (issuccess != -1) {
                if (askrecord.getIssuccess() != issuccess) {
                    continue;
                }
            }
            // 运行到这里，说明都满足条件了
            askrecords2.add(askrecord);
        }
        System.out.println("end size:" + askrecords2.size());
        return askrecords2;
    }

    // 查看数组中的元素是否被包含在某个字符串中
    private boolean isContain(String[] array, String str) {
        for (int i = 0; i < array.length; i++) {
            if (str.contains(array[i])) {
                return true;
            }
        }
        return false;
    }


    public ArrayList<Askrecord> queryAll() {
        return askrecordDao.queryAll();
    }

    public ArrayList<Askrecord> queryAllLimit(@Param("offset") int offet, @Param("limit") int limit) {
        return askrecordDao.queryAllLimit(offet, limit);
    }

    public ArrayList<Askrecord> queryBySuccess(int issuccess) {
        return askrecordDao.queryBySuccess(issuccess);
    }

    public ArrayList<Askrecord> queryRecordsBeforeTime(int lasthours) {
        if (lasthours == -1) {
            return queryAll();
        }
        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.HOUR_OF_DAY, -lasthours);//取当前时间之前的hours
        //通过格式化输出日期
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(cal.getTime());
        return askrecordDao.queryRecordsBeforeTime(time);
    }

    public int insert(Askrecord askrecord) {
        return askrecordDao.insert(askrecord);
    }

    public int update(Askrecord askrecord) {
        return askrecordDao.update(askrecord);
    }

    public Askrecord queryByPhone(String phone) {
        return askrecordDao.queryByPhone(phone);
    }

    public Askrecord queryById(long id) {
        return askrecordDao.queryById(id);
    }

    public int deleteById(long id) {
        return askrecordDao.deleteById(id);
    }
}
