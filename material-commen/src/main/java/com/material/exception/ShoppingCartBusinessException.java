package com.material.exception;

/**
 * 购物车异常
 */
public class ShoppingCartBusinessException extends BaseException {

    public ShoppingCartBusinessException(){}

    public ShoppingCartBusinessException(String msg){
        super(msg);
    }

}
