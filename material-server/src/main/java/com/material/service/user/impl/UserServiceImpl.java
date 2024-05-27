package com.material.service.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.material.constant.MessageConstant;
import com.material.context.BaseContext;
import com.material.dto.user.UserLoginDTO;
import com.material.dto.user.UserUpdateDTO;
import com.material.entity.User;
import com.material.exception.LoginFailedException;
import com.material.mapper.user.UserMapper;
import com.material.properties.WeChatProperties;
import com.material.result.Result;
import com.material.service.user.UserService;
import com.material.utils.CosUtil;
import com.material.utils.HttpClientUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private CosUtil cosUtil;
    @Resource
    private WeChatProperties weChatProperties;
    @Resource
    private UserMapper userMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) throws JsonProcessingException {
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        //如果是新用户，自动完成注册
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //返回这个用户对象
        return user;
    }

    /**
     * 更新用户信息
     * @param userUpdateDTO
     */
    @Override
    public void updateInfo(UserUpdateDTO userUpdateDTO) {
        userMapper.update(userUpdateDTO);
    }

    /**
     * 更新头像
     *
     * @param file
     * @return
     */
    @Override
    @Transactional
    public String uploadAvatar(MultipartFile file) throws IOException {
        // 获取用户头像路径
        User user = userMapper.getByUserId(BaseContext.getCurrentId());
        if (user.getAvatar() != null) {
            String fileUrl = user.getAvatar();
            // 截取文件名   https://user-1314771156.cos.ap-guangzhou.myqcloud.com/card4.jpg
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
            // 删除对应头像
            cosUtil.deleteUser(filename);
        }

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取原始文件名的后缀   dfdfdf.png
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 构造新文件名称
        String objectName = "avatar" + BaseContext.getCurrentId().toString() + extension;

        //文件的请求路径
        String filePath = cosUtil.uploadUser(file, objectName);

        // 数据库更新
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setAvatar(filePath);
        userMapper.update(userUpdateDTO);

        return filePath;



    }

    /**
     * 更新学生证
     *
     * @param file
     * @return
     */
    @Override
    @Transactional
    public String uploadCard(MultipartFile file) throws IOException {
        // 获取学生证图片路径
        User user = userMapper.getByUserId(BaseContext.getCurrentId());
        if (user.getStudentIdCardImage() != null) {
            String fileUrl = user.getStudentIdCardImage();
            // 截取文件名   https://user-1314771156.cos.ap-guangzhou.myqcloud.com/card4.jpg
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
            // 删除对应头像
            cosUtil.deleteUser(filename);
        }



        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取原始文件名的后缀   dfdfdf.png
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 构造新文件名称
        String objectName = "card" + BaseContext.getCurrentId().toString() + extension;

        //文件的请求路径
        String filePath = cosUtil.uploadUser(file, objectName);

        // 数据库更新
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setStudentIdCardImage(filePath);
        userMapper.update(userUpdateDTO);

        return filePath;
    }

    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code) throws JsonProcessingException {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JsonNode jsonNode = objectMapper.readTree(json);
        // 当请求返回错误时的处理
        if (jsonNode.get("errcode") != null) {
            // errcode为40163时，是code已经被使用了的错误
            if (jsonNode.get("errcode").asInt() == 40163){
                throw new LoginFailedException(MessageConstant.CODE_BEEN_USED);
            }

            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        String openid = jsonNode.get("openid").asText();
        return openid;
    }
}
