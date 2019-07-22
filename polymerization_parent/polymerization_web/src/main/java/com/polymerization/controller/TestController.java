package com.polymerization.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/22 18:12
 */
@RestController
public class TestController {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }
}
