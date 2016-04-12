package cn.com.fanyev5.baseservice.kafka.consumer.impl;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants;
import cn.com.fanyev5.baseservice.kafka.consumer.IKafkaConsumer;
import cn.com.fanyev5.baseservice.kafka.consumer.INamedKafkaConsumerService;
import cn.com.fanyev5.basecommons.service.CommonNamedResourceService;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Kafka consumer命名服务接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
public class NamedKafkaConsumerServiceImpl extends CommonNamedResourceService<IKafkaConsumer> implements INamedKafkaConsumerService {

    public NamedKafkaConsumerServiceImpl() {
        super(new KafkaConsumerLoaderImpl(
                JAXBUtil.unmarshal(KafkaConfigs.class, KafkaConstants.KAFKA_CONFIG_FILE_NAME)
        ), new RemovalListener<String, IKafkaConsumer>() {
            @Override
            public void onRemoval(RemovalNotification<String, IKafkaConsumer> notification) {
                if (notification != null) {
                    IKafkaConsumer consumer = notification.getValue();
                    if (consumer != null) {
                        consumer.destroy();
                    }
                }
            }
        });
    }
}
