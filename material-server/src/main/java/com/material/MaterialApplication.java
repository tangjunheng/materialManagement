package com.material;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.material.mapper")
@EnableCaching // 开启缓存注解功能
@EnableTransactionManagement // 开启注解方式的事务管理
@Slf4j
public class MaterialApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterialApplication.class, args);
		log.info("server started");
	}

}
