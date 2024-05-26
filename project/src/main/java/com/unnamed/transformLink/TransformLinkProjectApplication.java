package com.unnamed.transformLink;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.unnamed.transformLink.project.dao.mapper")
public class TransformLinkProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransformLinkProjectApplication.class);
    }
}
