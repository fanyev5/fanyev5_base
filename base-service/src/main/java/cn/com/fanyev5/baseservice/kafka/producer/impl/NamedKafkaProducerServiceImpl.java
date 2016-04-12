package cn.com.fanyev5.baseservice.kafka.producer.impl;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants;
import cn.com.fanyev5.baseservice.kafka.producer.IKafkaProducer;
import cn.com.fanyev5.baseservice.kafka.producer.INamedKafkaProducerService;
import cn.com.fanyev5.basecommons.service.CommonNamedResourceService;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Kafka producer命名服务接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class NamedKafkaProducerServiceImpl extends CommonNamedResourceService<IKafkaProducer> implements INamedKafkaProducerService {

    public NamedKafkaProducerServiceImpl() {
        super(new KafkaProducerLoaderImpl(
                JAXBUtil.unmarshal(KafkaConfigs.class, KafkaConstants.KAFKA_CONFIG_FILE_NAME)
        ), new RemovalListener<String, IKafkaProducer>() {
            @Override
            public void onRemoval(RemovalNotification<String, IKafkaProducer> notification) {
                if (notification != null) {
                    IKafkaProducer producer = notification.getValue();
                    if (producer != null) {
                        producer.destroy();
                    }
                }
            }
        });
    }

}
