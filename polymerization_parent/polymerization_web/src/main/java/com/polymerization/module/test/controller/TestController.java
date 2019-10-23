package com.polymerization.module.test.controller;

import com.polymerization.feign.ProviderFeignService;
import com.polymerization.module.test.model.Test;
import com.polymerization.module.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 18:12
 */
@RestController
public class TestController {

    @Autowired
    private ConsulRegistration consulRegistration;

    @Autowired
    private TestService testService;

    @RequestMapping("/")
    public String home() {
        System.out.println(consulRegistration.getInstanceId());
        System.out.println(consulRegistration.getService().toString());
        return "Hello World";
    }

    @RequestMapping("/findAll")
    public List<Test> findAll() {
        return testService.findAll();
    }

    @Autowired
    private ProviderFeignService providerFeginService;

    @RequestMapping("/testFegin")
    public String testFegin() {
        return providerFeginService.home();
    }
}
