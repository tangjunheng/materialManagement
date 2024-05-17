package com.material.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO  implements Serializable {
    private Long materialId;
    private Long setmealId;
    private Integer number;
}
