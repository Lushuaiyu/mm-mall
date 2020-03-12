package com.lushuaiyu.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by jones on 7/3/2020
 *
 * @author lushuaiyu
 */
@SpringBootApplication(exclude = JacksonAutoConfiguration.class)
@EnableTransactionManagement
@MapperScan(basePackages = "com.lushuaiyu.mall.mapper")
public class MallAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApplication.class);
    }
}
