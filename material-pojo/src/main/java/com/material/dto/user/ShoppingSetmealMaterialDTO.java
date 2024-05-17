package com.material.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingSetmealMaterialDTO implements Serializable {
    private Long materialId;
    private Long setmealId;
    private Integer number;
    //数量
    private Integer copies;
}
