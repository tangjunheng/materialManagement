package com.thomas.materialManagement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.thomas.materialManagement.mapper")
public class MaterialManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterialManagementApplication.class, args);
	}

}
