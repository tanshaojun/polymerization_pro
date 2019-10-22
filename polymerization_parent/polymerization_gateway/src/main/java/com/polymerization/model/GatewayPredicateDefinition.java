package com.polymerization.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 断言
 * @author tanshaojun
 * @version 1.0
 * @date 2019/10/22 16:20
 */
@Data
public class GatewayPredicateDefinition {
    /**
     * Filter Name
     */
    private String name;
    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}
