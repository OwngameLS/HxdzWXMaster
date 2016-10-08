package com.owngame.service.impl;

import com.owngame.dao.FunctionDao;
import com.owngame.entity.Function;
import com.owngame.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-27.
 */
@Service
public class FunctionServiceImpl implements FunctionService {
    @Autowired
    FunctionDao functionDao;

    public int createFunction(Function function) {
        return 0;
    }

    public ArrayList<Function> queryAll() {
        return functionDao.queryAll();
    }

    public int deleteById(long id) {
        return 0;
    }

    public int update(Function function) {
        return 0;
    }
}
