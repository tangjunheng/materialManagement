package com.material.config;


import com.material.interceptor.JwtTokenAdminInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    /**
     * 注册自定义拦截器
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/worker/login", "/admin/worker/register");

    }

    /**
     * 创建OpenAPI对象
     * @return OpenAPI
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        log.info("创建OpenAPI对象");
        return new OpenAPI()
                .info(new Info()
                        .title("物资管理系统管理端接口文档") // 接口文档标题
                        .description("这是基于Knife4j OpenApi3的物资管理系统的接口文档") // 接口文档简介
                        .version("1.0")  // 接口文档版本
                        .contact(new Contact()
                                .name("Thomas") //开发者
                                .email("1761174609@qq.com")
                        )
                ); // 开发者联系方式
    }
    /**
     * 添加静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}
