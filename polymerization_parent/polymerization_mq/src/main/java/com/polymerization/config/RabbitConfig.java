package com.polymerization.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 交换器的几种模式：
 * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
 * HeadersExchange ：通过添加属性key-value匹配
 * DirectExchange:按照routingkey分发到指定队列
 * TopicExchange:多关键字匹配
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 11:21
 */
@Configuration
public class RabbitConfig {

//-------------------------------------普通工作队列模式start-------------------------------------

    /**
     * @apiNote 普通工作队列模式
     */
    @Bean
    public Queue simpleQueue() {
        return new Queue("test_queue");
    }
//-------------------------------------普通工作队列模式end-------------------------------------

//-------------------------------------广播模式start-------------------------------------

    /**
     * @apiNote 广播模式 测试a
     */
    @Bean
    public Queue fanoutAQueue() {
        return new Queue("fanout.a");
    }

    /**
     * @apiNote 广播模式 测试b
     */
    @Bean
    public Queue fanoutBQueue() {
        return new Queue("fanout.b");
    }


    /**
     * @apiNote 定义交换器
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * 将定义的fanout队列与fanoutExchange交换机绑定
     *
     * @return
     */
    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(fanoutAQueue()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(fanoutBQueue()).to(fanoutExchange());
    }
    //-------------------------------------广播模式end-------------------------------------


    //-------------------------------------topic模式start-------------------------------------

    @Bean
    public Queue topicAQueue() {
        return new Queue("topic.a");
    }

    @Bean
    public Queue topicBQueue() {
        return new Queue("topic.b");
    }

    @Bean
    public Queue topicCQueue() {
        return new Queue("topic.c");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    /**
     * 只会接收topic.msg的消息
     *
     * @return
     */
    @Bean
    public Binding bindingTopicA() {
        return BindingBuilder.bind(topicAQueue()).to(topicExchange()).with("topic.msg");
    }

    /**
     * 会接收topic.a.msg,topic.b.msg,topic.c.msg的消息
     *
     * @return
     */
    @Bean
    public Binding bindingTopicB() {
        return BindingBuilder.bind(topicBQueue()).to(topicExchange()).with("topic.*.msg");
    }

    /**
     * 只会接收topic开头的消息
     *
     * @return
     */
    @Bean
    public Binding bindingTopicC() {
        return BindingBuilder.bind(topicCQueue()).to(topicExchange()).with("topic.#");
    }
    //-------------------------------------topic模式end-------------------------------------


    //-------------------------------------死信队列start-------------------------------------

    @Bean
    public Queue dlxQueue() {
        return new Queue("dlx.queue");
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("dlxExchange");
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with("dlx");
    }


    /**
     * @apiNote 死信测试队列
     */
    @Bean
    public Queue dlxTestQueue() {
        Map<String, Object> arguments = new HashMap<>(2);
        // 绑定该队列到私信交换机
        arguments.put("x-dead-letter-exchange", "dlxExchange");
        arguments.put("x-dead-letter-routing-key", "dlx");
        return new Queue("test.dlx.queue", true, false, false, arguments);
    }


    /**
     * 死信测试交换机
     *
     * @return
     */
    @Bean
    public DirectExchange dlxTestExchange() {
        return new DirectExchange("dlxTestExchange");
    }

    /**
     * 死信测试绑定
     *
     * @return
     */
    @Bean
    public Binding blxTestBinding() {
        return BindingBuilder.bind(dlxTestQueue())
                .to(dlxTestExchange())
                .with("dlxTest");
    }

    //-------------------------------------死信队列end-------------------------------------


}
