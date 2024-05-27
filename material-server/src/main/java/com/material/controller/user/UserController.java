package com.material.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.material.constant.JwtClaimsConstant;
import com.material.constant.MessageConstant;
import com.material.context.BaseContext;
import com.material.dto.user.UserLoginDTO;
import com.material.dto.user.UserUpdateDTO;
import com.material.entity.User;
import com.material.properties.JwtProperties;
import com.material.result.Result;
import com.material.service.user.UserService;
import com.material.utils.CosUtil;
import com.material.utils.JwtUtil;
import com.material.vo.user.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Tag(name = "用户相关接口")
public class UserController {

    @Resource
    private CosUtil cosUtil;
    @Resource
    private UserService userService;
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(
            description = "微信登录",
            summary = "微信登录"
    )
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) throws JsonProcessingException {
        log.info("微信用户登录：{}",userLoginDTO.getCode());

        //微信登录
        User user = userService.wxLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    @PostMapping("/update")
    @Operation(
            description = "修改用户信息",
            summary = "修改用户信息"
    )
    public Result<UserLoginVO> updateUserInfo(@RequestBody UserUpdateDTO userUpdateDTO) throws JsonProcessingException {
        log.info("修改用户信息,用户id:{}", BaseContext.getCurrentId());
        userService.updateInfo(userUpdateDTO);
        return Result.success();
    }


    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(MultipartFile file){
        log.info("头像上传：{}",file);
        try {
            String filePath = userService.uploadAvatar(file);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("学生证传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload/card")
    public Result<String> uploadCard(MultipartFile file){
        log.info("头像上传：{}",file);
        try {
            String filePath = userService.uploadCard(file);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("学生证传失败：{}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);


    }


}
