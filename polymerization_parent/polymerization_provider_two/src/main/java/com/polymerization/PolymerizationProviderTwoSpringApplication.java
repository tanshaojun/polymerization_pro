package com.polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PolymerizationProviderTwoSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationProviderTwoSpringApplication.class, args);
    }

}
