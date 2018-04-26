package com.huashengke.com.tools;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Result<T> {

    public static Result<String> ok() {
        Result<String> result = new Result<>();

        return result;
    }

    public static Result<String> error(int rc) {
        return error(rc, null, null);
    }

    public static Result<String>error(Throwable e){
        return error(-1, null, e);
    }

    public static Result<String>error(String msg){
        return error(-1,msg,null);
    }
    public static Result<String> error(String msg, int rc){
        return error(rc, msg, null);
    }

    public static Result<String> error(int rc, String msg) {
        return error(rc, msg, null);
    }

    public static Result<String> error(int rc, Throwable e) {
        return error(rc, null, e);
    }

    public static <T> Result<T> error(int rc, T msg, Throwable e) {
        Result<T> result = new Result<>();

        result.setRc(rc);

        result.setRet(msg);

        result.setException(e);

        return result;
    }

    public static <T> Result<T> result(T t) {
        Result<T> r = new Result<T>();

        r.setRet(t);

        return r;
    }

    /**
     * 返回的结果编码
     */
    @ApiModelProperty(value = "返回的结果编码，当结果不为0时，则为异常", required = true)
    private int rc;

    @ApiModelProperty(hidden = true)
    private Throwable exception;

    @ApiModelProperty(value = "返回的数据，也可能是错误数据")
    private T ret;

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable e) {
        this.exception = e;
    }

    public T getRet() {
        return ret;
    }

    public void setRet(T ret) {
        this.ret = ret;
    }
}