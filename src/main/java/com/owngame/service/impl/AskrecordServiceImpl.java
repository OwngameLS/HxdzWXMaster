package com.owngame.service.impl;

import com.owngame.dao.AskrecordDao;
import com.owngame.entity.Askrecord;
import com.owngame.entity.Pager;
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
    public Pager<Askrecord> handleQuery(int lasthours, int type, String askers, String functions, int issuccess, int pageSize, int targetPage) {
        try {
            ArrayList<Askrecord> askrecords = queryRecordsBeforeTime(lasthours);
            if (askrecords == null) {
                return null;
            } else if (askrecords.size() == 0) {
                return null;
            }
            // 筛选
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
            int totalRecords = askrecords2.size();// 筛选的结果总数
            int totalPages = totalRecords / pageSize;// 总页数
            if (totalRecords % pageSize != 0) {
                totalPages++;
            }
            if (targetPage > totalPages) {
                targetPage = totalPages;
            }
            int startIndex = (targetPage - 1) * pageSize;
            int endIndex = startIndex + pageSize;
            if (endIndex > totalRecords) {
                endIndex = totalRecords;
            }
            askrecords = null;
            askrecords = new ArrayList<Askrecord>();
            for (int i = startIndex; i < endIndex; i++) {
                askrecords.add(askrecords2.get(i));
            }
            Pager<Askrecord> pager = new Pager<Askrecord>(targetPage, pageSize, totalRecords, askrecords);
            return pager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public int countAll() {
        return askrecordDao.countAll();
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

    public int countRecordsBeforeTime(int lasthours) {
        if (lasthours == -1) {
            return countAll();
        }
        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.HOUR_OF_DAY, -lasthours);//取当前时间之前的hours
        //通过格式化输出日期
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(cal.getTime());
        return askrecordDao.countRecordsBeforeTime(time);
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
