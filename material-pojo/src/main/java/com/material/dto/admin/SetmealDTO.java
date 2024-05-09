package com.material.dto.admin;

import com.material.entity.SetmealMaterial;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDTO implements Serializable {

    private Long id;

    // 分类id
    private Long categoryId;

    // 套餐名称
    private String name;

    // 状态 0:停用 1:启用
    private Integer status;

    // 描述信息
    private String description;

    // 图片
    private String image;

    // 套餐物资关系表数组
    private List<SetmealMaterial> setmealMaterials = new ArrayList<>();

}
