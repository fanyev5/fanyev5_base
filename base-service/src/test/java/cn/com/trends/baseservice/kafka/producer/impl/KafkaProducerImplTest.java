package cn.com.trends.baseservice.kafka.producer.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.com.fanyev5.baseservice.kafka.producer.IKafkaProducer;
import cn.com.fanyev5.baseservice.kafka.producer.INamedKafkaProducerService;
import cn.com.fanyev5.baseservice.kafka.producer.impl.NamedKafkaProducerServiceImpl;

/**
 * KafkaProducerImpl Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaProducerImplTest {

    private final String PRODUCER_TEST_NAME = "producer-test-1";
    private final String TOPIC_TEST_NAME = "topic-test-1";

    private INamedKafkaProducerService namedKafkaProducerService; 

    @Before
    public void before() {
        // 初始化
        namedKafkaProducerService = new NamedKafkaProducerServiceImpl();
    }

    @After
    public void after() {
        // 关闭
        namedKafkaProducerService.destroy();
    }

    @Test
    //@Ignore
    public void testSend() {
        IKafkaProducer<String> producer = namedKafkaProducerService.get(PRODUCER_TEST_NAME);
        for (int i = 0; i < 10; i++) {
            producer.send(TOPIC_TEST_NAME, String.format("test_value_%s_%s", i, System.currentTimeMillis()));
        }
    }
}
