package com.polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class PolymerizationConfigSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationConfigSpringApplication.class, args);
    }

}
