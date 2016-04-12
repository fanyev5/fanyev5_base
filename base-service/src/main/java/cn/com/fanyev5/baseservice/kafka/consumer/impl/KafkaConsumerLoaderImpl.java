package cn.com.fanyev5.baseservice.kafka.consumer.impl;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.ConsumerConstants;
import cn.com.fanyev5.baseservice.kafka.consumer.IKafkaConsumer;
import cn.com.fanyev5.baseservice.kafka.exception.KafkaException;
import cn.com.fanyev5.baseservice.kafka.message.IKafkaMessageSerializer;
import cn.com.fanyev5.baseservice.kafka.message.impl.StringMessageSerializer;
import cn.com.fanyev5.baseservice.kafka.util.KafkaUtil;
import cn.com.fanyev5.basecommons.service.ServiceLoader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import kafka.consumer.ConsumerConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kafka consumer loader实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaConsumerLoaderImpl<T> extends ServiceLoader<IKafkaConsumer<T>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerLoaderImpl.class);

    private Map<String, KafkaConfigs.ZookeeperElement> zookeeperMap;
    private Map<String, KafkaConfigs.ConsumerElement> consumerMap;
    private Set<String> kafkaConsumers;

    public KafkaConsumerLoaderImpl(KafkaConfigs kafkaConfigs) {
        this.zookeeperMap = kafkaConfigs.getZookeepers();
        this.consumerMap = kafkaConfigs.getConsumers();
        this.kafkaConsumers = kafkaConfigs.getConsumers().keySet();
    }

    public KafkaConsumerLoaderImpl(KafkaConfigs kafkaConfigs, Set<String> kafkaConsumers) {
        this.zookeeperMap = kafkaConfigs.getZookeepers();
        this.consumerMap = kafkaConfigs.getConsumers();
        this.kafkaConsumers = kafkaConsumers;
    }

    /**
     * 取得kafkaConsumer的实例
     *
     * @param key
     * @return
     */
    @Override
    public IKafkaConsumer<T> load(final String key) throws KafkaException {
        Preconditions.checkState(kafkaConsumers.contains(key), "kafka consumer key [%s] is not set in app", key);
        KafkaConfigs.ConsumerElement consumerElement = this.consumerMap.get(key);
        Preconditions.checkNotNull(consumerElement, "kafka consumer [%s] config is null", key);
        String zookeeperKey = consumerElement.getZookeeper();
        KafkaConfigs.ZookeeperElement zookeeperElement = this.zookeeperMap.get(zookeeperKey);
        Preconditions.checkNotNull(zookeeperElement, "kafka consumer zookeeperElement [%s]-[%s] config is null", key, zookeeperKey);

        // 校验topic处理器
        List<KafkaConfigs.TopicElement> topicElements = consumerElement.getTopics();
        if (LOGGER.isDebugEnabled()) {
            for (KafkaConfigs.TopicElement element : topicElements) {
                LOGGER.debug(String.format("kafka groupId:[%s],topic:[%s],processor:[%s]", key, element.getName(), element.getName()));
            }
        }
        // consumer配置
        Map<String, String> configMap = Maps.newHashMap();
        // 默认的参数
        for (String[] entry : ConsumerConstants.DEFAULT_CONFIG) {
            configMap.put(entry[0], entry[1]);
        }
        // 程序员自己配置的参数
        Map<String, String> zkConfigs = zookeeperElement.getConfigs();
        for (Map.Entry<String, String> entry : zkConfigs.entrySet()) {
            configMap.put(entry.getKey(), entry.getValue());
        }
        Map<String, String> configs = consumerElement.getConfigs();
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            configMap.put(entry.getKey(), entry.getValue());
        }
        configMap.put("groupid", consumerElement.getName());
        // 必须如此设置的参数
        for (String[] entry : ConsumerConstants.FIXED_CONFIG) {
            configMap.put(entry[0], entry[1]);
        }

        ConsumerConfig consumerConfig = KafkaUtil.createConsumerConfig(configMap);
        // 初始化解码器
        IKafkaMessageSerializer serializer = null;
        if (StringUtils.isNotEmpty(consumerElement.getSerializer())) {
            String serializerClass = consumerElement.getSerializer();
            try {
                serializer = Class.forName(serializerClass).asSubclass(IKafkaMessageSerializer.class).newInstance();
                LOGGER.info(String.format("kafka [%s] serializer is [%s].", key, serializer.getClass().getName()));
            } catch (Exception e) {
                throw new KafkaException(e);
            }
        } else {
            serializer = new StringMessageSerializer();
        }
        return new KafkaConsumerImpl(key, consumerElement, serializer, consumerConfig);
    }
}
