package com.huashengke.com.tools.exception.live;


import com.huashengke.com.tools.exception.user.UserErrorRc;

/**
 * Created by Administrator on 2018/4/9.
 */
public class NoSuchLiveException extends LiveException {

    private UserErrorRc rc;
    public NoSuchLiveException(String msg,LiveErrorRc rc){
        super(msg,rc);
    }

    public int getRc() {
        return rc.getRc();
    }
}
