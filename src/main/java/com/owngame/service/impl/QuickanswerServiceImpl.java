package com.owngame.service.impl;

import com.owngame.dao.QuickanswerDao;
import com.owngame.entity.Keyword;
import com.owngame.entity.KeywordsResult;
import com.owngame.entity.Pager;
import com.owngame.entity.Quickanswer;
import com.owngame.service.KeywordService;
import com.owngame.service.QuickanswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/28.
 */
@Service
public class QuickanswerServiceImpl implements QuickanswerService {
    @Autowired
    QuickanswerDao quickanswerDao;
    @Autowired
    KeywordService keywordService;

    public ArrayList<Quickanswer> queryAll() {
        return quickanswerDao.queryAll();
    }

    public int countAll() {
        return quickanswerDao.countAll();
    }

    public Pager<Quickanswer> queryAllLimit(int pageSize, int targetPage) {
        int totalRecords = quickanswerDao.countAll();
        // 根据pageSize和targetPage整理得到 offset 和 limit
        int offset = (targetPage - 1) * pageSize;
        int limit = pageSize;
        ArrayList<Quickanswer> quickanswers = quickanswerDao.queryAllLimit(offset, limit);
        Pager<Quickanswer> pager = new Pager<Quickanswer>(targetPage, pageSize, totalRecords, quickanswers);
        return pager;
    }

    public Quickanswer getResult(String keyname) {
        return quickanswerDao.getResult(keyname);
    }

    /**
     * 查询question的结果
     * @param questions
     * @return
     */
    public String getResults(String questions){
        // 先分析questions
        questions = questions.replaceAll("，", ",");
        String ques[] = questions.split(",");
        String results = "";
        boolean hasMatched = false;
        for(int i=0;i<ques.length;i++){
            Quickanswer quickanswer = getResult(ques[i]);
            if (quickanswer == null){
                continue;
            }
            if (quickanswer.getEnable() == 0){
                continue;
            }
            if(quickanswer.getResult().equals("") == false){
                hasMatched = true;
                results += quickanswer.getResult();
            }
        }
        if(hasMatched){
            return results;
        }else {
            return null;
        }
    }


    public int insert(Quickanswer quickanswer) {
        return quickanswerDao.insert(quickanswer);
    }

    public int update(Quickanswer quickanswer) {
        return quickanswerDao.update(quickanswer);
    }

    public ArrayList<Quickanswer> queryLikeName(String keyname) {
        return quickanswerDao.queryLikeName(keyname);
    }

    public int deleteById(long id) {
        return quickanswerDao.deleteById(id);
    }

    // 检查关键字是否重复
    public String checkDuplicateKeywords(int id, String keyname) {
        KeywordsResult keywordsResult = keywordService.checkDuplicateKeywords(id, keyname, Keyword.TYPE_QUICKANSWER);
        if (keywordsResult.getIsSuccess() > 0) {
            return "notduplicate";
        } else {
            return keywordsResult.getSimilarKeys();
        }
    }

    public String getHelp(){
        ArrayList<Keyword> keywords = queryAllKeywords();
        String helpResult = "";
        for(Keyword keyword : keywords){
            helpResult += "[" + keyword.getKeywords()+"]可查询“"+keyword.getDescription()+"”\n";
        }
        return helpResult;
    }

    public ArrayList<Keyword> queryAllKeywords() {
        return quickanswerDao.queryAllKeywords();
    }

//    private String checkDuplicate(String keyname) {
//        ArrayList<Quickanswer> quickanswers = queryLikeName("%" + keyname + "%");
//        if (quickanswers == null) {
//            return "notduplicate";
//        }
//        if (quickanswers.size() == 0) {
//            return "notduplicate";
//        }
//        boolean isDuplicated = false;
//        String duplicateWords = "";
//        for (int i = 0; i < quickanswers.size(); i++) {
//            if (keyname.equals(quickanswers.get(i).getKeyname())) {
//                isDuplicated = true;
//            } else {
//                duplicateWords += quickanswers.get(i).getKeyname() + ";";
//            }
//        }
//        if (isDuplicated) {
//            String result = "关键字【" + keyname + "】已存在！";
//            if (duplicateWords.equals("") == false) {
//                result += "与它类似的关键字有：" + duplicateWords;
//            }
//            return result;
//        } else {
//            return "notduplicate";
//        }
//    }
}
