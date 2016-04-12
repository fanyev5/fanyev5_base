package cn.com.fanyev5.baseservice.kafka.producer.impl;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.ProducerConstants;
import cn.com.fanyev5.baseservice.kafka.exception.KafkaException;
import cn.com.fanyev5.baseservice.kafka.producer.IKafkaProducer;
import cn.com.fanyev5.baseservice.kafka.producer.handler.KafkaAsyncEventHandler;
import cn.com.fanyev5.baseservice.kafka.util.KafkaUtil;
import cn.com.fanyev5.basecommons.service.ServiceLoader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Kafka producer loader实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaProducerLoaderImpl<T> extends ServiceLoader<IKafkaProducer<T>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerLoaderImpl.class);

    private Map<String, KafkaConfigs.ZookeeperElement> zookeeperMap;
    private Map<String, KafkaConfigs.ProducerElement> producerMap;
    private Set<String> kafkaProducers;

    public KafkaProducerLoaderImpl(KafkaConfigs kafkaConfigs) {
        this.zookeeperMap = kafkaConfigs.getZookeepers();
        this.producerMap = kafkaConfigs.getProducers();
        this.kafkaProducers = kafkaConfigs.getProducers().keySet();
    }

    public KafkaProducerLoaderImpl(KafkaConfigs kafkaConfigs, Set<String> kafkaProducers) {
        this.zookeeperMap = kafkaConfigs.getZookeepers();
        this.producerMap = kafkaConfigs.getProducers();
        this.kafkaProducers = kafkaProducers;
    }

    @Override
    public IKafkaProducer<T> load(final String key) throws KafkaException {
        Preconditions.checkState(kafkaProducers.contains(key), "kafka producer key [%s] is not set in app");
        KafkaConfigs.ProducerElement producerElement = this.producerMap.get(key);
        Preconditions.checkNotNull(producerElement, "kafka producer [%s] config is null", key);
        String zookeeperKey = producerElement.getZookeeper();
        KafkaConfigs.ZookeeperElement zookeeperElement = this.zookeeperMap.get(zookeeperKey);
        Preconditions.checkNotNull(zookeeperElement, "kafka producer zookeeperElement [%s]-[%s] config is null", key, zookeeperKey);
        Map<String, String> configMap = initKafkaConfig(zookeeperElement, producerElement);
        // 注册事件处理器,替换系统默认的,以增加失败日志的记录
        configMap.put("event.handler", KafkaAsyncEventHandler.class.getName());
        List<String> eventHandlerProps = Lists.newArrayList();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)) {
                // 替换,为其它字串,因为kafka解析event.handler时会根据,做分隔
                eventHandlerProps.add(name + '=' + KafkaUtil.encodeConfigValue(value));
            }
        }
        if (!eventHandlerProps.isEmpty()) {
            configMap.put("event.handler.props", Strings.join(eventHandlerProps, ","));
        }
        ProducerConfig producerConfig = KafkaUtil.createProducerConfig(configMap);
        final Producer<String, T> producer = new Producer<String, T>(producerConfig);
        KafkaProducerImpl<T> kafkaProducer = new KafkaProducerImpl<T>(key, producer);
        LOGGER.info("success init kafka producer.key:[{}]", key);
        return kafkaProducer;
    }

    /**
     * 初如化配置文件
     *
     * @param zookeeperElement
     * @param producerElement
     * @return
     */
    private Map<String, String> initKafkaConfig(final KafkaConfigs.ZookeeperElement zookeeperElement,
                                                final KafkaConfigs.ProducerElement producerElement) {
        Map<String, String> configMap = Maps.newHashMap();
        // 默认的参数
        for (String[] entry : ProducerConstants.DEFAULT_CONFIG) {
            configMap.put(entry[0], entry[1]);
        }
        Map<String, String> zkConfigs = zookeeperElement.getConfigs();
        for (Map.Entry<String, String> entry : zkConfigs.entrySet()) {
            configMap.put(entry.getKey(), entry.getValue());
        }
        String serializer = producerElement.getSerializer();
        configMap.put("serializer.class", serializer);
        Map<String, String> configs = producerElement.getConfigs();
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            configMap.put(entry.getKey(), entry.getValue());
        }
        return configMap;
    }
}
