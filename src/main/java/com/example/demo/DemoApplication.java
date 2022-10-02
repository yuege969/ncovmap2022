package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.demo")
//@MapperScan("com.example.demo.mapper")
@MapperScan({"com.example.demo.mapper","com.example.demo.dao"})
@EnableScheduling
public class DemoApplication {
	public static void main(String[] args) {
		//SendMail.sendMail("yuege_969@163.com","hello");
		SpringApplication.run(DemoApplication.class, args);
	}
}
