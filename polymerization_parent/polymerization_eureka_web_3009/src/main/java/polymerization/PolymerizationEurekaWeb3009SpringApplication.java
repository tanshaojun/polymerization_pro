package polymerization;

import myrule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;


/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 15:07
 */
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "EUREKA-PROVIDER", configuration = MySelfRule.class)//name 访问服务的名称  configuration  重写的配置文件
public class PolymerizationEurekaWeb3009SpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolymerizationEurekaWeb3009SpringApplication.class, args);
    }
}
