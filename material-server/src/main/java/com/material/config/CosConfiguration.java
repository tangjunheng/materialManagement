package com.material.config;

import com.material.properties.CosProperties;
import com.material.utils.CosUtil;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建CosUtil对象
 */
@Configuration
@Slf4j
public class CosConfiguration {

    @Resource
    private CosProperties cosProperties;
    /**
     * 初始化COSClient
     * @return
     */
    @Bean
    public CosUtil initCos(){
        log.info("初始化COS客户端");

        // 1 初始化用户身份信息（secretId, secretKey）
        BasicCOSCredentials credentials = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        // 2 设置 bucket 的区域, COS 地域的简称请参照
        Region region = new Region(cosProperties.getRegionName());
        ClientConfig clientConfig = new ClientConfig(region);
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 以下的设置，是可选的：
        // 设置 socket 读取超时，默认 30s
        clientConfig.setSocketTimeout(30*1000);
        // 设置建立连接超时，默认 30s
        clientConfig.setConnectionTimeout(30*1000);
        // 3 生成 cos 客户端。
        return new CosUtil(credentials, clientConfig,cosProperties);
    }

}
