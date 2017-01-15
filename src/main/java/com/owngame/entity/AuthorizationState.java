package com.owngame.entity;

/**
 * 系统授权状态
 * Created by Administrator on 2017/1/14.
 */
public class AuthorizationState {
    String valid;
    String invalidReason;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
    }
}
