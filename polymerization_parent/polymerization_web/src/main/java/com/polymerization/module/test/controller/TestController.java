package com.polymerization.module.test.controller;

import com.polymerization.module.test.model.Test;
import com.polymerization.module.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TestService testService;

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }

    @RequestMapping("/findAll")
    public List<Test> findAll() {
        return testService.findAll();
    }
}
