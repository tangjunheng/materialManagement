package com.material.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 套餐id
    private Long setmealId;

    // 物资id
    private Long materialId;

    // 物资名称 （冗余字段）
    private String name;

    //数量
    private Integer copies;
}
