package com.material.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml中的配置
 */
@Component
@ConfigurationProperties(prefix = "material.cos")
@Data
public class CosProperties {
    private String appid ;
    // 密钥id
    private String secretId;
    // 密钥key
    private String SecretKey;
    // 桶名称
    private String bucketName;
    // 访问域名
    private String cosPath;
    // 地域名称
    private String regionName;


}

