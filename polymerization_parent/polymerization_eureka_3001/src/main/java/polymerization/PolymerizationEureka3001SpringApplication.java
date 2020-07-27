package polymerization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableEurekaServer
public class PolymerizationEureka3001SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationEureka3001SpringApplication.class, args);
    }
}
