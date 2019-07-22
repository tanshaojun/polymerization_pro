package com.polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PolymerizationConsulSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationConsulSpringApplication.class, args);
    }

}
