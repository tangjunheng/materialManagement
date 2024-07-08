package com.material.vo.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //待接单的订单数量
    private Integer toBeConfirmed;

    //待派送的订单数量
    private Integer confirmed;

    //物资准备完毕的订单数量
    private Integer materialsReady;

    //用户使用中的订单数量
    private Integer usingMaterials;

    //用户归还了的订单数量
    private Integer returnMaterials;

    //已确认归还物资的订单数量(已完成)
    private Integer returnMaterialsConfirmed;

    //已取消的订单数量
    private Integer canceled;

    //初始化为0
    public OrderStatisticsVO(){
        toBeConfirmed = confirmed = materialsReady = usingMaterials = returnMaterials = returnMaterialsConfirmed = canceled =0;
    }
}
