package com.material.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersExceptionDTO implements Serializable {

    private Long id;
    //订单出现的问题
    private String exceptionMessage;

}
