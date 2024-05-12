package com.material.dto.admin;

import lombok.Data;

import java.io.Serializable;

@Data
public class MaterialPageQueryDTO implements Serializable {

    // 页码
    private int page;

    // 每页记录数
    private int pageSize;

    // 分类名称
    private String name;

    // 分类类型id
    private Integer categoryId;

    // 状态 0表示禁用 1表示启用
    private Integer status;


}
