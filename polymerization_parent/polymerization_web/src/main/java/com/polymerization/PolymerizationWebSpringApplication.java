package com.polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
@MapperScan("com.polymerization.module.*.mapper")
public class PolymerizationWebSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationWebSpringApplication.class, args);
    }

}
