package com.material.vo.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerLoginVO implements Serializable {

    private Long id;

    private String userName;

    private String name;

    private String token;
}
