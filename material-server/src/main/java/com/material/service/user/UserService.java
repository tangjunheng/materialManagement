package com.material.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.dto.user.UserLoginDTO;
import com.material.entity.User;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO) throws JsonProcessingException;
}
