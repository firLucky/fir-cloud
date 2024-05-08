package com.fir.seata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = "com.fir.seata.mapper")
@Configuration
// Feign全局注解
@EnableFeignClients
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 禁用Ping Method
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }
}
