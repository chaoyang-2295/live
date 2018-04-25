package com.huashengke.com.tools.exception.live;

/**
 * Created by Administrator on 2018/4/9.
 */
public enum LiveErrorRc {

    NullError(2000),

    NoSuchLiveError(2001),

    LiveStatusError(2002),

    LiveChapterTimeError(2003),

    NoSuchLiveStreamError(2004),

    TextFormatError(2005),

    NoSuchVideoError(2006),

    NoSuchChapterError(2007),

    AliyunRequstError(2008),

    NoSuchLiveRoom(2009);

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
