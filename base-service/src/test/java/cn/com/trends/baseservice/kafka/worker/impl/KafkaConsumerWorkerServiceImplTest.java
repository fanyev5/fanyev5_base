package cn.com.trends.baseservice.kafka.worker.impl;

import com.google.common.collect.Sets;

import cn.com.fanyev5.baseservice.kafka.consumer.INamedKafkaConsumerService;
import cn.com.fanyev5.baseservice.kafka.consumer.impl.NamedKafkaConsumerServiceImpl;
import cn.com.fanyev5.baseservice.kafka.worker.IKafkaConsumerWorkerService;
import cn.com.fanyev5.baseservice.kafka.worker.impl.KafkaConsumerWorkerServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * KafkaConsumerWorkerServiceImpl Test
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
public class KafkaConsumerWorkerServiceImplTest {

    private final String CONSUMER_TEST_NAME = "consumer-test-1";

    private IKafkaConsumerWorkerService kafkaConsumerWorkerService;

    private INamedKafkaConsumerService namedKafkaConsumerService;

    @Before
    public void before() {
        // 初始化
        namedKafkaConsumerService = new NamedKafkaConsumerServiceImpl();
        kafkaConsumerWorkerService = new KafkaConsumerWorkerServiceImpl(namedKafkaConsumerService);

        kafkaConsumerWorkerService.execStart(Sets.newHashSet(CONSUMER_TEST_NAME));
    }

    @After
    public void after() {
        // 关闭
        kafkaConsumerWorkerService.execStop();
    }

    @Test
    //@Ignore
    public void test() {
        try {
            Thread.sleep(10 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
