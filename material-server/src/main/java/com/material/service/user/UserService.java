package com.material.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.dto.user.UserLoginDTO;
import com.material.dto.user.UserUpdateDTO;
import com.material.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO) throws JsonProcessingException;

    /**
     * 更新用户信息
     * @param userUpdateDTO
     */
    void updateInfo(UserUpdateDTO userUpdateDTO);

    /**
     * 更新头像
     * @param file
     * @return
     */
    String uploadAvatar(MultipartFile file) throws IOException;

    /**
     * 更新学生证
     * @param file
     * @return
     */
    String uploadCard(MultipartFile file) throws IOException;

}
