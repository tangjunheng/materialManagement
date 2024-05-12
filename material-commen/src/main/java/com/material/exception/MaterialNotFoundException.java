package com.material.exception;

/**
 * 物资不存在异常
 */
public class MaterialNotFoundException extends BaseException {

    public MaterialNotFoundException() {
    }
    public MaterialNotFoundException(String msg) {
        super(msg);
    }
}
