package cn.com.trends.baseservice.kafka.consumer.impl;

import kafka.consumer.KafkaStream;
import org.junit.*;

import cn.com.fanyev5.baseservice.kafka.consumer.IKafkaConsumer;
import cn.com.fanyev5.baseservice.kafka.consumer.INamedKafkaConsumerService;
import cn.com.fanyev5.baseservice.kafka.consumer.impl.NamedKafkaConsumerServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * KafkaConsumerImpl Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaConsumerImplTest {

    private final String CONSUMER_TEST_NAME = "consumer-test-1";

    private INamedKafkaConsumerService namedKafkaConsumerService;

    @Before
    public void before() {
        // 初始化
        namedKafkaConsumerService = new NamedKafkaConsumerServiceImpl();
    }

    @After
    public void after() {
        // 关闭
        namedKafkaConsumerService.destroy();
    }

    @Test
    //@Ignore
    public void test() {
        Assert.assertNotNull(namedKafkaConsumerService);
        IKafkaConsumer<String> kafkaConsumer = namedKafkaConsumerService.get(CONSUMER_TEST_NAME);
        Map<String, List<KafkaStream<String>>> messageStreams = kafkaConsumer.getMessageStreams();
        Assert.assertNotNull(messageStreams);
    }
}
