package com.polymerization.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态增加路由
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/10/22 16:23
 */
@Slf4j
@RestController
@RequestMapping("/route")
public class RouteController {


    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;


    /**
     * 获取网关所有的路由信息
     *
     * @return
     */
    @RequestMapping("/routes")
    public Flux<RouteDefinition> getRouteDefinitions() {
        return routeDefinitionLocator.getRouteDefinitions();
    }


    /**
     * 增加路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        String flag = "fail";
        try {
            RouteDefinition definition = assembleRouteDefinition(gatewayRouteDefinition);
            flag = this.dynamicRouteService.add(definition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id) {
        try {
            return this.dynamicRouteService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新路由
     *
     * @param gatewayRouteDefinition
     * @return
     */
    @PostMapping("/update")
    public String update(@RequestBody GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = assembleRouteDefinition(gatewayRouteDefinition);
        return this.dynamicRouteService.update(definition);
    }

    /**
     * 把传递进来的参数转换成路由对象
     *
     * @param gatewayRouteDefinition
     * @return
     */
    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRouteDefinition.getId());
        definition.setOrder(gatewayRouteDefinition.getOrder());

        //设置断言
        List<PredicateDefinition> predicates = new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList = gatewayRouteDefinition.getPredicates();
        for (GatewayPredicateDefinition gatewayPredicateDefinition : gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gatewayPredicateDefinition.getArgs());
            predicate.setName(gatewayPredicateDefinition.getName());
            predicates.add(predicate);
        }
        definition.setPredicates(predicates);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gatewayRouteDefinition.getFilters();
        for (GatewayFilterDefinition gatewayFilterDefinition : gatewayFilters) {
            FilterDefinition filter = new FilterDefinition();
            filter.setName(gatewayFilterDefinition.getName());
            filter.setArgs(gatewayFilterDefinition.getArgs());
            filters.add(filter);
        }
        definition.setFilters(filters);

        URI uri = null;
        if (gatewayRouteDefinition.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(gatewayRouteDefinition.getUri()).build().toUri();
        } else {
            // uri为 lb://xxxx-xxxx 时使用下面的方法
            uri = URI.create(gatewayRouteDefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }

}
