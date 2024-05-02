package com.material.exception;


/**
 * 业务异常
 */
public class BaseException extends RuntimeException{
    public BaseException() {
    }

    /**
     * 异常构造方法 在使用时方便传入错误码和信息
     */
    public BaseException(String msg) {
        super(msg);
    }
}
