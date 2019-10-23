package com.polymerization.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/10/23 10:56
 */
@Configuration
public class FeignConfig {
    /**
     * 日志level有4个级别
     * 1.NONE，不记录任何日志
     * 2.BASIC，仅记录请求方法、URL以及响应状态码和执行时间
     * 3.HEADRES，除了BASIC以外的还会记录请求和响应的头信息
     * 4.FULL,所有
     *
     * @return
     */
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }
}
