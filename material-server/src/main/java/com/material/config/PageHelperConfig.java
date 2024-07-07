package com.material.config;


import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PageHelper 分页配置
 */
@Configuration
public class PageHelperConfig {

    @Bean
    public Interceptor[] plugins() {
        return new Interceptor[]{new PageInterceptor()};
    }

}
