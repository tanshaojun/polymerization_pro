package polymerization.module.test.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "EUREKA-PROVIDER", fallback = TestFallbackService.class)
public interface TestOpenFeignService {

    @RequestMapping("/eureka_provider")
    String home();
}
