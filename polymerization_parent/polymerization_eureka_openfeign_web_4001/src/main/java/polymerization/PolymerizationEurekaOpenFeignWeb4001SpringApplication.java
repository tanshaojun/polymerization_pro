package polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class PolymerizationEurekaOpenFeignWeb4001SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationEurekaOpenFeignWeb4001SpringApplication.class, args);
    }
}
