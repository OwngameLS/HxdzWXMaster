package com.owngame.entity;

/**
 * Created by Administrator on 2017/3/7.
 * 与关键字有关的类
 * function 和 quickanswer 中都有涉及
 */
public class Keyword {
    public static final int TYPE_FUNCTION = 0, TYPE_QUICKANSWER = 1;
    long id;
    String keywords;
    String description;
    int type = 0;// 从哪里来的 0 function, 1 quickanswer

    public Keyword(long id, String keywords, String description, int type) {
        this.id = id;
        this.keywords = keywords;
        this.description = description;
        this.type = type;
    }

    public Keyword(Integer id, String keywords, String description, Long type) {
        this.id = id.intValue();
        this.keywords = keywords;
        this.description = description;
        this.type = type.intValue();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
