package com.material.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class MaterialDTO implements Serializable {
    private Long id;
    // 物资名称
    private String name;
    // 物资分类id
    private Long categoryId;
    // 图片
    private String image;
    // 描述信息
    private String description;
    // 分类状态 0:禁用，1:启用
    private Integer status;
    // 数量
    private Integer numbers;
}
