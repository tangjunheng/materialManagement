package com.material.dto.user;


import lombok.Data;

@Data
public class UserUpdateDTO {

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

    // 头像
    private String avatar;

}
