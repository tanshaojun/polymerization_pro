package com.polymerization.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * rabbit消息生产者
 *
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 11:23
 */

@Component
public class RabbitConsumer {
    private Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    /**
     * 消息消费
     *
     * @RabbitHandler 代表此方法为接受到消息后的处理方法
     */
    @RabbitHandler
    @RabbitListener(queues = "test_queue")
    public void simpleQueueRecieved(String msg, Channel channel, Message message) {
        try {
            logger.info("simpleQueueRecieved message a:{}", msg);
            //确认消息
            /**
             * deliveryTag:该消息的index
             * multiple：是否批量. true：将一次性ack所有小于deliveryTag的消息。
             * 消费者成功处理后，调用对消息进行确认。
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                /**
                 * 批量拒绝
                 * deliveryTag:该消息的index。
                 *  multiple：是否批量. true：将一次性拒绝所有小于deliveryTag的消息。
                 *  requeue：被拒绝的是否重新入队列。
                 */
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
                //单条拒绝
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
                //消息拒绝后，再次发布消息
//                channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
//                        message.getMessageProperties().getReceivedRoutingKey(),
//                        MessageProperties.PERSISTENT_TEXT_PLAIN,
//                        message.getBody());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "fanout.a")
    public void fanoutARecieved(String msg, Channel channel, Message message) {
        try {
            logger.info("fanoutARecieved message a:{}", msg);
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //丢弃消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "fanout.b")
    public void fanoutBRecieved(String msg, Channel channel, Message message) {
        try {
            logger.info("fanoutBRecieved message b:{}", msg);
            //确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                //丢弃消息
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "topic.a")
    public void topicARecieved(String msg) {
        logger.info("topicARecieved message a:{}", msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "topic.b")
    public void topicBRecieved(String msg) {
        logger.info("topicBRecieved message b:{}", msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "topic.c")
    public void topicCRecieved(String msg) {
        logger.info("topicCRecieved message c:{}", msg);
    }


    @RabbitHandler
    @RabbitListener(queues = "dlx.queue")
    public void dlxRecieved(String msg, Channel channel, Message message) {
        logger.info("dlxRecieved message c:{}", msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "test.dlx.queue")
    public void testDlxRecieved(String msg, Channel channel, Message message) {
        logger.info("testDlxRecieved message c:{}", msg);
        try {
            //不确认消息，则消息进入死信队列
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
