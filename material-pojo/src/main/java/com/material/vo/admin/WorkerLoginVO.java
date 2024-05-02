package com.material.vo.admin;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工登录返回数据")
public class WorkerLoginVO implements Serializable {

    @Schema(description = "员工ID")
    private Long id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "JWT令牌")
    private String token;
}
