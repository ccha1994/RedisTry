package com.yunweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yun.service","com.yun.dao","com.yunweb.controller"})
@MapperScan("com.yun.dao")
@EnableCaching
public class TestWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestWebApplication.class, args);
    }
}
