package com.material.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "修改员工信息")
public class WorkerDTO implements Serializable {
    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

}
