package cn.com.trends.baseservice.kafka.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.fanyev5.baseservice.kafka.processor.IKafkaMessageProcessor;

/**
 * 测试消息处理器
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class TestKafkaMessageProcessor implements IKafkaMessageProcessor<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestKafkaMessageProcessor.class);

    @Override
    public boolean process(String topic, String msg) {
        LOGGER.info("==> topic: {}, msg: {}", topic, msg);
        return true;
    }

}