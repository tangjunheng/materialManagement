package com.material.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 订单id
    private Long orderId;

    // 名称
    private String name;

    // 物资id
    private Long materialId;

    // 套餐id
    private Long setmealId;

    // 数量
    private Integer number;

    //图片
    private String image;
}
