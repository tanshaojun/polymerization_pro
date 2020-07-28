package polymerization.module.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polymerization.module.test.service.TestOpenFeignService;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 18:12
 */
@RestController
public class TestController {

    @Autowired
    private TestOpenFeignService testOpenFeignService;

    @RequestMapping("/eurekaWeb")
    public String home() {
        String response = testOpenFeignService.home();
        return response;
    }


}
