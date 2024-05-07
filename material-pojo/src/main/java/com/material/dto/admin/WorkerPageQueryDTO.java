package com.material.dto.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "员工分页查询请求参数")
public class WorkerPageQueryDTO implements Serializable {

    @Schema(description = "员工姓名")
    private String name;

    @Schema(description = "页码")
    private int page;

    @Schema(description = "每页的大小")
    private int pageSize;

}
