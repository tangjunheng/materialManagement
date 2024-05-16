package com.material.interceptor;


import com.material.constant.JwtClaimsConstant;
import com.material.context.BaseContext;
import com.material.properties.JwtProperties;
import com.material.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 拦截器  校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("员工jwt校验:{}", token);
            // 密钥解密
            Jws<Claims> claimsJws = JwtUtil.parseJWT(token, jwtProperties.getAdminSecretKey());
            Long farId = Long.valueOf(claimsJws.getPayload().get(JwtClaimsConstant.WORK_ID).toString());
            log.info("当前员工id:{}",farId);
            BaseContext.setCurrentId(farId); //设置当前登录的用户id
            //放行
            return true;

        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
