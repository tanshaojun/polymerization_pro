package com.polymerization.feign;

import org.springframework.stereotype.Service;

/**
 * feign启用hystrix，才能熔断、降级
 * 1.配置文件加
 * feign.hystrix.enabled= true
 * 2.启动类加@EnableHystrix注解
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/10/23 10:48
 */
@Service
public class ProviderFeignFallbackService implements ProviderFeignService {
    @Override
    public String home() {
        return "error";
    }
}
