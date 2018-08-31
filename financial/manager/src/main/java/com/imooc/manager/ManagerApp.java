package com.imooc.manager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * 管理端启动类
 * Created by Administrator on 2018/7/18 0018.
 */
@SpringBootApplication
@EntityScan(basePackages = {"com.imooc.entity"})
public class ManagerApp {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApp.class);
    }
}
