package com.material.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    //订单状态 1待处理 2已接单 3物资准备完毕 4用户使用物资 5用户归还物资 6确认物资归还状况 7已取消
    private Integer status;

}
