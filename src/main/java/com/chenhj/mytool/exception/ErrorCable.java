package com.chenhj.mytool.exception;



/**
 * Created by yuanwai (<a href="mailto:lxdhzzj@gmail.com">lxdhzzj@gmail.com</a>)
 * Create Time: 2015/7/8 17:23
 * Description:
 */
public class ErrorCable {

    private Integer code;
    private String message;

    public ErrorCable(Integer errCode, String errMsg) {
        this.code = errCode;
        this.message = errMsg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
