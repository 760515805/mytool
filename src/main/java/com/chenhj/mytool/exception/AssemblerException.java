package com.chenhj.mytool.exception;


/**
 * Created by yuanwai (<a href="mailto:lxdhzzj@gmail.com">lxdhzzj@gmail.com</a>)
 * Create Time: 2015/6/12 17:52
 * Description:
 */
public class AssemblerException extends RuntimeException {

    public AssemblerException() {
        super();
    }

    public AssemblerException(String message) {
        super(message);
    }

    public AssemblerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssemblerException(Throwable cause) {
        super(cause);
    }

    protected AssemblerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AssemblerException(ErrorCable errorCable){
        super(errorCable.getCode()+"|"+errorCable.getMessage());
    }
}
