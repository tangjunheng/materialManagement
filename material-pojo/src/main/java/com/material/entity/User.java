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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //微信用户唯一标识
    private String openid;

    //姓名
    private String name;

    //手机号
    private String phone;

    // 学号
    private String idNumber;

    // 所属学院
    private String institute;

    // 学生证图片路径
    private String studentIdCardImage;

    // 头像路径
    private String avatar;

    // 注册时间
    private LocalDateTime createTime;
}
