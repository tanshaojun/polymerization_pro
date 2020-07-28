package polymerization.module.test.service;

import org.springframework.stereotype.Component;

@Component
public class TestFallbackService implements TestOpenFeignService {


    @Override
    public String home() {
        return "error";
    }
}
