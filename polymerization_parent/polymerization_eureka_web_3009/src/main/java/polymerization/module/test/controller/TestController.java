package polymerization.module.test.controller;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 18:12
 */
@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://EUREKA-PROVIDER";

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/eurekaWeb")
    public String home() {
        String response = restTemplate.getForObject(url + "/eureka_provider", String.class);
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("EUREKA-PROVIDER");
        return response;
    }

}
