package com.wmn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.wmn.service")
@ComponentScan(basePackages = "com.wmn.mapper")


public class WmnBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(WmnBlogApplication.class, args);
	}

}
