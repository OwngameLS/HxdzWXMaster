package com.owngame.entity;

import java.sql.Date;

/**
 * 五项指标
 * Created by Administrator on 2016-8-19.
 */
public class Wxzb {
    public float sw;
    public float kr;
    public float rk;
    public float ck;
    public float jyl;
    public Date date;

    public float getSw() {
        return sw;
    }

    public void setSw(float sw) {
        this.sw = sw;
    }

    public float getKr() {
        return kr;
    }

    public void setKr(float kr) {
        this.kr = kr;
    }

    public float getRk() {
        return rk;
    }

    public void setRk(float rk) {
        this.rk = rk;
    }

    public float getCk() {
        return ck;
    }

    public void setCk(float ck) {
        this.ck = ck;
    }

    public float getJyl() {
        return jyl;
    }

    public void setJyl(float jyl) {
        this.jyl = jyl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
