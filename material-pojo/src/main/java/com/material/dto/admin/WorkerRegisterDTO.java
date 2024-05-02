package com.material.dto.admin;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Schema(description = "员工注册请求参数")
public class WorkerRegisterDTO implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    // 创建对象时赋值UUID
    @JsonIgnore
    private String salt = UUID.randomUUID().toString().replaceAll("-", "");

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别")
    private String sex;


}
