package com.polymerization.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 17:02
 */
public class MyReturnCallback implements RabbitTemplate.ReturnCallback {
    private Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("replyCode:{}", replyCode);
        logger.info("replyText:{}", replyText);
        logger.info("exchange:{}", exchange);
        logger.info("routingKey:{}", routingKey);
    }
}
