package com.huashengke.com.tools.exception.live;


import com.huashengke.com.tools.exception.HuaShengKeException;

/**
 * Created by Administrator on 2018/4/9.
 */
public class LiveException extends HuaShengKeException {

    private LiveErrorRc rc;

    public LiveException(String msg,LiveErrorRc rc){
        super(msg);
        this.rc = rc;
    }

    public int getRc() {
        return rc.getRc();
    }
}
