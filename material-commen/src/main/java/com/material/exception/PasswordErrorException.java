package com.material.exception;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException() {
    }
    public PasswordErrorException(String passwordError) {
        super(passwordError);
    }
}
