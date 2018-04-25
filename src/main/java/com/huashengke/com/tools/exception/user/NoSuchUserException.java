package com.huashengke.com.tools.exception.user;


import com.huashengke.com.tools.exception.HuaShengKeException;

/**
 * Created by Administrator on 2018/4/9.
 */
public class NoSuchUserException extends HuaShengKeException {

    private UserErrorRc rc;

    public NoSuchUserException(String msg,UserErrorRc rc){
        super(msg);
        this.rc = rc;
    }

    public int getRc() {
        return rc.getRc();
    }
}
