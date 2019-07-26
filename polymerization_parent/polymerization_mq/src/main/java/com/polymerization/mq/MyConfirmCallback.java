package com.polymerization.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 17:01
 */
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback {
    private Logger logger = LoggerFactory.getLogger(RabbitProducer.class);


    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("消息唯一标识correlationData：{}", correlationData);
        logger.info("确认结果ack：{}", ack);
        logger.info("失败原因cause：{}", cause);

    }
}
