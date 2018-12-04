package com.cheng.es.common;

/**
 * 返回结果常量
 *
 * @author fengcheng
 * @version 2017/3/14
 */
public enum ResultCode {

    SUCCESS("0", "处理成功"),
    FAILD("10000", "处理失败"),
    FAILD_PARAM("20000", "参数错误"),;

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
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
}
