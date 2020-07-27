package polymerization.module.test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 18:12
 */
@RestController
public class TestController {

    @Value("${server.port}")
    private int port;

    @RequestMapping("/eureka_provider")
    public String home() {
        return port + ",Hello World";
    }

}
