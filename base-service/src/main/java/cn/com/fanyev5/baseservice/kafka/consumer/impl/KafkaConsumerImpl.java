package cn.com.fanyev5.baseservice.kafka.consumer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.consumer.IKafkaConsumer;
import cn.com.fanyev5.baseservice.kafka.message.IKafkaMessageSerializer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Kafka consumer实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaConsumerImpl<T> implements IKafkaConsumer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerImpl.class);
    private final String name;
    /**
     * consumer的配置
     */
    private KafkaConfigs.ConsumerElement consumerElement;
    /**
     * 解码器
     */
    private IKafkaMessageSerializer<T> messageSerializer;
    /**
     * 消费者配置
     */
    private ConsumerConfig consumerConfig;

    /**
     * 客户端连接
     */
    private List<ConsumerConnector> consumerConnectors = Lists.newArrayList();
    /**
     * 消息流
     */
    private Map<String, List<KafkaStream<T>>> messageStreams = Maps.newHashMap();

    private final Object lock = new Object();
    private volatile boolean inited = false;

    public KafkaConsumerImpl(String name, KafkaConfigs.ConsumerElement consumerElement, IKafkaMessageSerializer messageSerializer, ConsumerConfig consumerConfig) {
        this.name = name;
        this.consumerElement = consumerElement;
        this.messageSerializer = messageSerializer;
        this.consumerConfig = consumerConfig;
    }

    @Override
    public void destroy() {
        if (inited) {
            for (ConsumerConnector consumerConnector : this.consumerConnectors) {
                try {
                    consumerConnector.shutdown();
                } catch (Exception e) {
                    LOGGER.error("failed shutdown kafka consumer {} connector.", name, e);
                }
            }
        }
    }

    /**
     * 取得kafka的consumer的stream,key是topic
     *
     * @return
     */
    @Override
    public Map<String, List<KafkaStream<T>>> getMessageStreams() {
        synchronized (lock) {
            if (!inited) {
                this.inited = true;
                List<KafkaConfigs.TopicElement> topics = this.consumerElement.getTopics();
                if (topics.size() > 0) {
                    Map<String, Integer> topicCountMap = Maps.newHashMap();
                    for (KafkaConfigs.TopicElement topic : topics) {
                        topicCountMap.put(topic.getName(), topic.getStreamcount());
                    }
                    LOGGER.info(String.format("kafka consumer topics [%s].", topicCountMap.toString()));
                    ConsumerConnector consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
                    Map<String, List<KafkaStream<T>>> topicStreams = consumerConnector.createMessageStreams(topicCountMap, messageSerializer);
                    this.consumerConnectors.add(consumerConnector);
                    this.messageStreams.putAll(topicStreams);
                }
            }
        }
        return messageStreams;
    }

}
