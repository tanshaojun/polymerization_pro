package com.polymerization.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * rabbit消息生产者
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 11:23
 */
@Component
public class RabbitProducer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 简单模式
     */
    public void simpleQueueSend() {
        this.rabbitTemplate.setReturnCallback(new MyReturnCallback());
        this.rabbitTemplate.setConfirmCallback(new MyConfirmCallback());
        this.rabbitTemplate.convertAndSend("test_queue", "hello world");
    }

    /**
     * 广播模式
     */
    public void fanoutExchangeSend() {
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", "fanoutExchange hello world");
    }

    /**
     * 消息确认
     */
    public void fanoutExchangeSendACK() {
        this.rabbitTemplate.setReturnCallback(new MyReturnCallback());
        this.rabbitTemplate.setConfirmCallback(new MyConfirmCallback());
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", "hello world");
    }

    /**
     * topic模式
     *
     * @param routingKey
     */
    public void topicExchangeSend(String routingKey) {
        this.rabbitTemplate.convertAndSend("topicExchange", routingKey, "topicExchange hello world");
    }

}
