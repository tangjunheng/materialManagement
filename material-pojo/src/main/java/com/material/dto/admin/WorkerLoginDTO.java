package com.material.dto.admin;

import lombok.Data;

import java.io.Serializable;


@Data
public class WorkerLoginDTO implements Serializable {

    private String username;

    private String password;

}
