package com.huashengke.com.tools.exception.user;

/**
 * Created by Administrator on 2018/4/9.
 */
public enum UserErrorRc {

    NullError(1000),

    NoSuchUserError(1001),

    TelephoneHasBound(1002),

    AccountAlreadyExist(1003),

    PasswordError(1004);

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
