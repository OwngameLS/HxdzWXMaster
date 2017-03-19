package com.owngame.service.impl;

import com.owngame.entity.Keyword;
import com.owngame.entity.KeywordsResult;
import com.owngame.service.FunctionService;
import com.owngame.service.KeywordService;
import com.owngame.service.QuickanswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/7.
 */
@Service
public class KeywordsServiceImpl implements KeywordService {
    @Autowired
    FunctionService functionService;
    @Autowired
    QuickanswerService quickanswerService;

    HashSet<String> allKeywordsFunction = null;
    HashSet<String> allKeywordsQuickanswer = null;

    /**
     * 检查一个关键字是否与已存在的关键字冲突
     *
     * @param id      关键字的id,<0表示更新
     * @param keyname 关键字内容，A,A1,A2 这样的组合
     * @param type    关键字类型，function或者quickanswer
     * @return
     */
    public KeywordsResult checkDuplicateKeywords(long id, String keyname, int type) {
        // 先得到所有的关键字信息
        ArrayList<Keyword> keywordsFunction = functionService.queryAllKeywords();
        ArrayList<Keyword> keywordsQuickanswer = quickanswerService.queryAllKeywords();
        // 将所有关键字整理成一个HashSet，必须所有都查出来
        allKeywordsFunction = arrayToSet(keywordsFunction);
        allKeywordsQuickanswer = arrayToSet(keywordsQuickanswer);
        // 根据类型进行检查（尽量不做多以操作）
        KeywordsResult keywordsResult = null;
        if (type == Keyword.TYPE_FUNCTION) {
            if(keywordsFunction == null || keywordsFunction.size() == 0){
                keywordsResult = checkDuplicate(id, keyname, type, keywordsQuickanswer);
            }else {
                keywordsResult = checkDuplicate(id, keyname, type, keywordsFunction);
            }
        } else if (type == Keyword.TYPE_QUICKANSWER) {
            if(keywordsQuickanswer == null || keywordsQuickanswer.size() == 0){
                keywordsResult = checkDuplicate(id, keyname, type, keywordsFunction);
            }else {
                keywordsResult = checkDuplicate(id, keyname, type, keywordsQuickanswer);
            }
        }
        return keywordsResult;
    }

    // 检查关键字是否重复
    private KeywordsResult checkDuplicate(long id, String keyname, int type, ArrayList<Keyword> keywords) {
        KeywordsResult keywordsResult = new KeywordsResult();
        if (keywords == null) {
            return keywordsResult;
        }
        if (keywords.size() == 0) {
            return keywordsResult;
        }
        // 将新关键字拆分（先去重，怕出现用户捣乱 A,A这样的）
        String nks[] = uniqueStringArray(keyname.split(","));
        int arrayType = keywords.get(0).getType();
        String similarKeys = "";

        if (id < 0) {// 一、整体新建关键字
            String result = checkAll(nks);
            if (result.equals("") == false) {
                keywordsResult.setIsSuccess(-1);
                keywordsResult.setSimilarKeys(result);
            }
            return keywordsResult;
        }
        // 二、更新关键字
        Keyword keyword = null;
        for (int i = 0; i < keywords.size(); i++) {
            // 先找到所在的ID对象
            if (keywords.get(i).getId() == id) {
                keyword = keywords.get(i);
                break;
            }
        }
        if (keyname.equals(keyword.getKeywords())) {// 没有变化，直接返回
            return keywordsResult;
        }
        // 仔细判断
        // 找出在旧关键字中不存在的部分（即是新的）
        nks = findNew(nks, stringToSet(keyword.getKeywords()));
        // 查询是否有冲突
        String result = checkAll(nks);
        if (result.equals("") == false) {// 查询到了重复
            keywordsResult.setIsSuccess(-1);
            keywordsResult.setSimilarKeys(result);
        }
        return keywordsResult;
    }

    /**
     * 查询某个关键字数组中的关键字是否有重复的
     *
     * @param keys
     * @return
     */
    private String checkAll(String keys[]) {
        String similarKeys = "";
        for (int x = 0; x < keys.length; x++) {// 遍历循环新关键字是否有重复
            KeywordsResult tempkr = checkAll(keys[x]);
            if (tempkr.getIsSuccess() < 0) {
                similarKeys += tempkr.getSimilarKeys();
            }
        }
        return similarKeys;
    }


    /**
     * 查找keywords在全部关键字中的重复情况
     * 当新建某个关键字时使用
     *
     * @param newKeyword 新关键字（不是有逗号分隔的关键字组合）
     * @return
     */
    private KeywordsResult checkAll(String newKeyword) {
        // 查询所有的关键字
        // 分别检查
        KeywordsResult tempKR1 = checkInAppointedSet(newKeyword, allKeywordsFunction);
        KeywordsResult tempKR2 = checkInAppointedSet(newKeyword, allKeywordsQuickanswer);
        KeywordsResult tempKR = new KeywordsResult();
        if (Math.min(tempKR1.getIsSuccess(), tempKR2.getIsSuccess()) < 0) {// 如果二者结果有一个小于0，说明结果是不成功的
            tempKR.setIsSuccess(-1);
            System.out.println("11:" + tempKR1.getSimilarKeys() + ";; 22:" + tempKR2.getSimilarKeys());
            String tSimilarKeys = "";
            if(tempKR1.getIsSuccess()<0){
                // 查询allKeywordsQuickanswer中是否有类似的
                tSimilarKeys = tempKR1.getSimilarKeys() + checkSimilarInSet(newKeyword, allKeywordsQuickanswer);
            }else if(tempKR2.getIsSuccess()<0){
                // 查询allKeywordsFunction中是否有类似的
                tSimilarKeys = tempKR1.getSimilarKeys() + checkSimilarInSet(newKeyword, allKeywordsFunction);
            }
            tempKR.setSimilarKeys(tSimilarKeys);
        }
        return tempKR;
    }

    /**
     * 在指定的Set中查找是否重复
     *
     * @param keyword
     * @param set
     * @return
     */
    private KeywordsResult checkInAppointedSet(String keyword, HashSet<String> set) {
        KeywordsResult tempKR = new KeywordsResult();
        boolean isIn = set.contains(keyword);
        String similarKey = "";
        boolean hasSimilar = false;
        if (isIn) {
            tempKR.setIsSuccess(-1);
            // 查找类似关键字
            similarKey += "关键字【" + keyword + "】已经存在。";
            for (String str : set) {// 因为重复了，所以要遍历所有的。
                if (str.contains(keyword) && str.equals(keyword) == false) {// 仅仅是相似，而不是相同
                    if (hasSimilar == false) {// 是第一次找到相似的
                        similarKey += "类似的有：" + str + ";";
                        hasSimilar = true;
                    } else {// 前面已经找到相似的了，直接添加在结尾处
                        similarKey += str + ";";
                    }
                }
            }
            tempKR.setSimilarKeys(similarKey);
        }
        return tempKR;
    }

    private String checkSimilarInSet(String keyword, HashSet<String> set){
        String similarKey = "";
        for (String str : set) {// 因为重复了，所以要遍历所有的。
            if (str.contains(keyword)) {// 包含即是相似
                similarKey += str + ";";
            }
        }
        return similarKey;
    }

    /**
     * 将ArrayList类型的关键字整理成一个HashSet
     *
     * @param keywords
     * @return
     */
    private HashSet<String> arrayToSet(ArrayList<Keyword> keywords) {
        HashSet<String> ks = new HashSet<String>();
        if (keywords == null) {
            return ks;
        }
        if (keywords.size() == 0) {
            return ks;
        }
        for (Keyword keyword : keywords) {
            String k[] = keyword.getKeywords().split(",");
            for (int i = 0; i < k.length; i++) {
                ks.add(k[i]);
            }
        }
        return ks;
    }


    /**
     * 将String类型的关键字组合整理成一个HashSet
     *
     * @param keywordsStr
     * @return
     */
    private HashSet<String> stringToSet(String keywordsStr) {
        HashSet<String> ks = new HashSet<String>();
        String kss[] = keywordsStr.split(",");
        for (String keyword : kss) {
            ks.add(keyword);
        }
        return ks;
    }

    /**
     * 去除数组内部的重复
     *
     * @param array
     * @return
     */
    public static String[] uniqueStringArray(String[] array) {
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }
        String[] arrayResult = (String[]) set.toArray(new String[set.size()]);
        return arrayResult;
    }

    /**
     * 找到新关键字中新的部分
     *
     * @param newStrs
     * @param oldStrs
     * @return
     */
    private String[] findNew(String newStrs[], HashSet<String> oldStrs) {
        ArrayList<String> newStrings = new ArrayList<String>();
        for (int i = 0; i < newStrs.length; i++) {
            if (oldStrs.contains(newStrs[i]) == false) {// 不存在则添加
                newStrings.add(newStrs[i]);
            }
        }
        String ts[] = new String[newStrings.size()];
        for (int i = 0; i < newStrings.size(); i++) {
            ts[i] = newStrings.get(i);
        }
        return ts;
    }

}
