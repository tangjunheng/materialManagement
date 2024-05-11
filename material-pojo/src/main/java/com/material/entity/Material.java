package com.material.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material  implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer number;
    //创建时间
    private LocalDateTime createTime;
    //更新时间
    private LocalDateTime updateTime;
    //创建人
    private Long createUser;
    //修改人
    private Long updateUser;

}
