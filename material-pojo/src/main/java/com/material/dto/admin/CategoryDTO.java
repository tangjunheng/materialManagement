package com.material.dto.admin;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    // 主键
    private Long id;

    // 类型 1 物资分类 2 套餐
    private Integer type;

    // 分类名称
    private String name;

    // 排序
    private Integer sort;
}
