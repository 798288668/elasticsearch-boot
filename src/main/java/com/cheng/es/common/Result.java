package com.cheng.es.common;

import java.io.Serializable;

/**
 * 结果返回
 *
 * @author lufengc
 * @version 2016/12/26
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code = ResultCode.SUCCESS.getCode();
    private String message = ResultCode.SUCCESS.getMessage();

    private Object data;

    /**
     * 静态构造器
     *
     * @param resultCode 响应编码
     * @return 构造器对象
     */
    public static Result of(ResultCode resultCode) {
        Result result = new Result();
        result.code = resultCode.getCode();
        result.message = resultCode.getMessage();
        return result;
    }

    /**
     * 静态构造器
     *
     * @param data 返回结果
     * @return 构造器对象
     */
    public static Result of(Object data) {
        Result result = new Result();
        result.code = ResultCode.SUCCESS.getCode();
        result.message = ResultCode.SUCCESS.getMessage();
        result.data = data;
        return result;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
