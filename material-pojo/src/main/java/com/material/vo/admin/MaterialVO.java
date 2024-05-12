package com.material.vo.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "物资数据")
public class MaterialVO {
    private Long id;
    // 物资名称
    private String name;
    // 物资分类id
    private Long categoryId;
    // 图片
    private String image;
    // 描述信息
    private String description;
    // 0 启用 1 停用
    private Integer status;
    // 数量
    private Integer number;
    // 更新时间
    private LocalDateTime updateTime;
    // 分类名称
    private String categoryName;

}
