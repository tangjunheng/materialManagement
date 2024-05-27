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
    private String secretKey;
    // 地域名称
    private String regionName;
    // 物资桶名称
    private String materialBucketName;
    // 物资访问域名
    private String materialCosPath;
    // 用户桶名称
    private String userBucketName;
    // 用户访问域名
    private String userCosPath;


}

