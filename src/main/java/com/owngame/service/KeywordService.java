package com.owngame.service;

import com.owngame.entity.KeywordsResult;

/**
 * Created by Administrator on 2017/3/7.
 * 检查关键字是否重复的服务
 */
public interface KeywordService {
    public KeywordsResult checkDuplicateKeywords(long id, String keyname, int type);
}
