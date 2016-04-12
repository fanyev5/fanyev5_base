package cn.com.fanyev5.baseservice.kafka.producer.impl;

import cn.com.fanyev5.baseservice.kafka.producer.IKafkaProducer;
import cn.com.fanyev5.basecommons.codec.JSONUtil;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Kafka producer实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaProducerImpl<T> implements IKafkaProducer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerImpl.class);

    /**
     * 名称
     */
    private String name;

    /**
     * Kafka producer实例
     */
    private Producer<String, T> producer;

    public KafkaProducerImpl(String name, Producer<String, T> producer) {
        this.name = name;
        this.producer = producer;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void send(String topic, T data) {
        try {
            producer.send(new ProducerData<String, T>(topic, data));
        } catch (Exception e) {
            String dataJson = JSONUtil.obj2Json(data);
            LOGGER.error(String.format("kafka send error.topic:[%s],data:[%s]", topic, dataJson), e);
        }
    }

    @Override
    public void send(String topic, List<T> datas) {
        try {
            producer.send(new ProducerData<String, T>(topic, datas));
        } catch (Exception e) {
            String dataJson = JSONUtil.obj2Json(datas);
            LOGGER.error(String.format("kafka send error.topic:[%s],data:[%s]", topic, dataJson), e);
        }
    }

    @Override
    public void destroy() {
        try {
            producer.close();
        } catch (Exception e) {
            LOGGER.error("failed shutdown kafka producer {} connector.", this.getName(), e);
        }
    }

}
