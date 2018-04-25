package com.huashengke.com.tools.exception.live;

/**
 * Created by Administrator on 2018/4/9.
 */
public enum LiveErrorRc {

    NullError(2000),

    NoSuchLiveError(2001),

    LiveStatusError(2002),

    LiveCustomeError(2003),

    NoSuchLiveStreamError(2004),

    TextFormatError(2005),

    AliyunRequstError(2006),

    NoSuchLiveRoom(2007);

    /**
     * 异常返回码
     */
    private int rc;
    LiveErrorRc(int rc){
        this.rc = rc;
    }

    public int getRc() {
        return rc;
    }
}
