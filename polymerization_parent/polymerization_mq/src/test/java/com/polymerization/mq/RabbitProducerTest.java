package com.polymerization.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tanshaojun
 * @version 1.0
 * @date 2019/7/26 11:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitProducerTest {

    @Autowired
    private RabbitProducer producer;

    /**
     * 普通工作队列模式
     */
    @Test
    public void testSimpleQueueSend() {
        producer.simpleQueueSend();
    }

    /**
     * fanout 广播模式
     */
    @Test
    public void testFanoutExchangeSend() {
        producer.fanoutExchangeSend();
    }

    /**
     * fanout 广播模式
     */
    @Test
    public void testFanoutExchangeSendACK() {
        producer.fanoutExchangeSendACK();
    }

    /**
     * topic模式
     */
    @Test
    public void testTopicExchangeSend() {
        /**
         * topic.msg:a和c能收到消息
         * topic.test:c能收到消息
         * topic.test.msg:b和c能收到消息
         */
        producer.topicExchangeSend("topic.test.msg");
    }

    /**
     * 死信测试
     */
    @Test
    public void dlxTestExchangeSend() {
        producer.dlxTestExchangeSend("dlxTest");
    }
}
