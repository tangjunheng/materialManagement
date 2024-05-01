package com.material.dto.admin;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class WorkerRegisterDTO implements Serializable {

    private String username;

    private String password;

    // 创建对象时赋值UUID
    @JsonIgnore
    private String salt = UUID.randomUUID().toString().replaceAll("-", "");

    private String name;

    private String phone;

    private String sex;


}
