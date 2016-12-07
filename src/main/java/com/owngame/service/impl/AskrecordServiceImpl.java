package com.owngame.service.impl;

import com.owngame.dao.AskrecordDao;
import com.owngame.entity.Askrecord;
import com.owngame.service.AskrecordService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-7.
 */
@Service
public class AskrecordServiceImpl implements AskrecordService{
    @Autowired
    AskrecordDao askrecordDao;

    public ArrayList<Askrecord> queryAll() {
        return askrecordDao.queryAll();
    }

    public ArrayList<Askrecord> queryAllLimit(@Param("offset") int offet, @Param("limit") int limit) {
        return askrecordDao.queryAllLimit(offet, limit);
    }

    public ArrayList<Askrecord> queryBySuccess(int issuccess) {
        return askrecordDao.queryBySuccess(issuccess);
    }

    public ArrayList<Askrecord> queryRecordssBeforeTime(String time) {
        return askrecordDao.queryRecordssBeforeTime(time);
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
