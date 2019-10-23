package com.polymerization.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * feign测试service
 * 1.启动类加@EnableFeignClients注解
 * name等于注册的服务者id
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/10/23 10:35
 */
@FeignClient(name = "provider-service", fallback = ProviderFeignFallbackService.class)
public interface ProviderFeignService {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    String home();

}
