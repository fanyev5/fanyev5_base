package cn.com.fanyev5.baseservice.kafka.worker.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.fanyev5.baseservice.kafka.exception.KafkaException;
import cn.com.fanyev5.baseservice.kafka.producer.IKafkaProducer;
import cn.com.fanyev5.baseservice.kafka.producer.INamedKafkaProducerService;
import cn.com.fanyev5.baseservice.kafka.worker.IKafkaProducerWorkerService;

import java.util.Set;

/**
 * Kafka producer工作服务接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class KafkaProducerWorkerServiceImpl implements IKafkaProducerWorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerWorkerServiceImpl.class);

    /**
     * Kafka producer命名服务
     */
    private INamedKafkaProducerService namedKafkaProducerService;

    public KafkaProducerWorkerServiceImpl(INamedKafkaProducerService namedKafkaProducerService) {
        this.namedKafkaProducerService = namedKafkaProducerService;
    }

    @Override
    public void execStart(Set<String> producerNames) {
        LOGGER.info("Starting KafkaProducerWorkerService...");
        for (String name : producerNames) {
            try {
                final IKafkaProducer kafkaProducer = namedKafkaProducerService.get(name);
                if (kafkaProducer == null) {
                    throw new KafkaException(String.format("kafka producer[%s] has no config.", name));
                }
            } catch (Exception e) {
                throw new KafkaException(String.format("kafka producer[%s] init error.", name), e);
            }
        }
        LOGGER.info("Starting KafkaProducerWorkerService,finish.");
    }

    @Override
    public void execStop() {
        LOGGER.info("Stoping KafkaProducerWorkerService...");
        try {
            namedKafkaProducerService.destroy();
        } catch (Exception e) {
            LOGGER.error("Stoping KafkaProducerWorkerService fail.", e);
        }
        LOGGER.info("Stoping KafkaProducerWorkerService[{}],finish.");
    }

}
