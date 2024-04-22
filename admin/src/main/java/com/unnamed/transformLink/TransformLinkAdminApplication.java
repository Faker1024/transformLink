package com.unnamed.transformLink;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.unnamed.transformLink.admin.dao.mapper")
public class TransformLinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransformLinkAdminApplication.class);
    }
}
