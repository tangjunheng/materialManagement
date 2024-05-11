package com.material.exception;

/**
 * 更新错误
 */
public class UpdateFailedException  extends BaseException{
    public UpdateFailedException(){}

    public UpdateFailedException(String msg){
        super(msg);
    }
}
