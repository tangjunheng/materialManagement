package com.material.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //名称
    private String name;

    //菜品id
    private Long materialId;

    //套餐id
    private Long setmealId;

    //数量
    private Integer number;

    //图片
    private String image;

    //用户id
    private Long userId;

    private LocalDateTime createTime;
}
