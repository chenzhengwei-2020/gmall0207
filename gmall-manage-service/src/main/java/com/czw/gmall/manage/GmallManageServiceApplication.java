package com.czw.gmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan("com.czw.gmall")
@MapperScan(basePackages = "com.czw.gmall.manage.mapper")
public class GmallManageServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(GmallManageServiceApplication.class, args);
    }

}
