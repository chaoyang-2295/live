package com.huashengke.com.tools.exception.user;



/**
 * Created by Administrator on 2018/4/9.
 */
public class NoSuchUserException extends UserException {

    private UserErrorRc rc;

    public NoSuchUserException(String msg,UserErrorRc rc){
        super(msg, rc);
        this.rc = rc;
    }

    public int getRc() {
        return rc.getRc();
    }
}
