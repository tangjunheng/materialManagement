package com.material.exception;

/**
 * 修改状态错误，
 */
public class StatusErrorException  extends BaseException{
    public StatusErrorException(){}

    public StatusErrorException(String msg){
        super(msg);
    }
}
