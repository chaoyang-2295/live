package com.huashengke.com.tools.exception.user;

/**
 * Created by Administrator on 2018/4/9.
 */
public enum UserErrorRc {

    NoSuchUserError(1000),

    TelephoneHasBound(1001);

    /**
     * 异常返回码
     */
    private int rc;

    UserErrorRc(int rc){
        this.rc = rc;
    }

    public int getRc() {
        return rc;
    }
}
