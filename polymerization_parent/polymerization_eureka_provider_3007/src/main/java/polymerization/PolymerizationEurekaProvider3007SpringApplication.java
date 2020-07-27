package polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableEurekaClient
public class PolymerizationEurekaProvider3007SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationEurekaProvider3007SpringApplication.class, args);
    }
}
