package com.material.dto.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
@Schema(description = "修改员工密码")
public class WorkerEditPasswordDTO implements Serializable {
    private Long id;
    private String password;
    // 创建对象时赋值UUID
    @JsonIgnore
    private String salt = UUID.randomUUID().toString().replaceAll("-", "");
}
