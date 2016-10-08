package com.owngame.dao;

import java.util.ArrayList;

/**
 * 因为具体管理有quartz自己的管理工具，这里只是为方便自己查询
 * Created by Administrator on 2016-9-19.
 */
public interface Qrtz_triggersDao {
    /**
     * 查询所有的名字
     *
     * @return
     */
    ArrayList<String> getNames();

}
