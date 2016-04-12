package cn.com.fanyev5.baseservice.kafka.worker.impl;

import cn.com.fanyev5.baseservice.base.config.xml.kafka.KafkaConfigs;
import cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants;
import cn.com.fanyev5.baseservice.kafka.consumer.IKafkaConsumer;
import cn.com.fanyev5.baseservice.kafka.consumer.INamedKafkaConsumerService;
import cn.com.fanyev5.baseservice.kafka.exception.KafkaException;
import cn.com.fanyev5.baseservice.kafka.processor.IKafkaMessageProcessor;
import cn.com.fanyev5.baseservice.kafka.util.KafkaUtil;
import cn.com.fanyev5.baseservice.kafka.worker.IKafkaConsumerWorkerService;
import cn.com.fanyev5.basecommons.spring.SpringContextManager;
import cn.com.fanyev5.basecommons.thread.NamedThreadFactory;
import cn.com.fanyev5.basecommons.util.StringUtil;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

/**
 * Kafka consumer工作服务接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
public class KafkaConsumerWorkerServiceImpl implements IKafkaConsumerWorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerWorkerServiceImpl.class);

    private final ThreadFactory threadFactory = new NamedThreadFactory("KafkaConsumer", true);

    /**
     * Kafka consumer命名服务
     */
    private INamedKafkaConsumerService namedKafkaConsumerService;

    public KafkaConsumerWorkerServiceImpl(INamedKafkaConsumerService namedKafkaConsumerService) {
        this.namedKafkaConsumerService = namedKafkaConsumerService;
    }

    @Override
    public void execStart(Set<String> consumerNames) {
        LOGGER.info("Starting KafkaConsumerWorkerService...");
        for (String name : consumerNames) {
            try {
                final IKafkaConsumer kafkaConsumer = namedKafkaConsumerService.get(name);
                if (kafkaConsumer == null) {
                    throw new KafkaException(String.format("kafka consumer[%s] has no config.", name));
                }
            } catch (Exception e) {
                throw new KafkaException(String.format("kafka consumer[%s] init error.", name), e);
            }
        }
        // Topic对应消息处理实现
        final KafkaConfigs kafkaConfigs = JAXBUtil.unmarshal(KafkaConfigs.class, KafkaConstants.KAFKA_CONFIG_FILE_NAME);
        Map<String, IKafkaMessageProcessor> messageProcessorMap = buildMessageProcessor(kafkaConfigs.getConsumers(), consumerNames);

        List<Runnable> processorRunnables = Lists.newArrayList();
        for (String name : consumerNames) {

            final IKafkaConsumer consumer = namedKafkaConsumerService.get(name);
            Map<String, List<KafkaStream>> topicStreams = consumer.getMessageStreams();

            for (Map.Entry<String, List<KafkaStream>> entry : topicStreams.entrySet()) {
                String topic = entry.getKey();
                List<KafkaStream> streams = entry.getValue();
                IKafkaMessageProcessor messageProcessor = messageProcessorMap.get(KafkaUtil.genMessageProcessorKey(name, topic));

                for (KafkaStream stream : streams) {
                    processorRunnables.add(new ConsumerProcessorRunnable(name, topic, stream, messageProcessor));
                }
            }
        }

        // 启动任务子线程
        for (Runnable processorRunnable : processorRunnables) {
            threadFactory.newThread(processorRunnable).start();
        }

        LOGGER.info("Starting KafkaConsumerWorkerService,finish.");
    }

    @Override
    public void execStop() {
        LOGGER.info("Stoping KafkaConsumerWorkerService...");
        try {
            namedKafkaConsumerService.destroy();
        } catch (Exception e) {
            LOGGER.error("Stoping KafkaConsumerWorkerService fail.", e);
        }
        LOGGER.info("Stoping KafkaConsumerWorkerService[{}],finish.");
    }

    /**
     * 构建消息处理实现Map
     *
     * @param consumers
     * @param consumerNames
     * @return
     */
    private Map<String, IKafkaMessageProcessor> buildMessageProcessor(Map<String, KafkaConfigs.ConsumerElement> consumers, Set<String> consumerNames) {
        Map<String, IKafkaMessageProcessor> messageProcessorMap = Maps.newHashMap();
        for (String groupId : consumerNames) {
            KafkaConfigs.ConsumerElement groupElement = consumers.get(groupId);
            Preconditions.checkArgument(groupElement != null, String.format("kafka groupId:[%s] is not configured in configure file.", groupId));
            List<KafkaConfigs.TopicElement> topics = groupElement.getTopics();
            for (KafkaConfigs.TopicElement topicElement : topics) {
                String topic = topicElement.getName();
                KafkaConfigs.ProcessorElement processorElement = topicElement.getProcessor();
                try {
                    Class<?> clazz = Class.forName(processorElement.getClassName()).asSubclass(IKafkaMessageProcessor.class);
                    Class<? extends IKafkaMessageProcessor> processor = (Class<? extends IKafkaMessageProcessor>) clazz;


                    IKafkaMessageProcessor messageProcessor = null;
                    if (KafkaConstants.KAFKA_PROCESSOR_MODEL_SRPING.equalsIgnoreCase(processorElement.getModel())) {
                        // 约束Processor SpringBean名称为类名,且首字母小写
                        String beanName = processor.getSimpleName();
                        beanName = StringUtil.toLowerByFirstChar(beanName);
                        messageProcessor = SpringContextManager.getBean(beanName, processor);
                    } else {
                        messageProcessor = processor.newInstance();
                    }
                    messageProcessorMap.put(KafkaUtil.genMessageProcessorKey(groupId, topic), messageProcessor);
                    LOGGER.info(String.format("kafka groupId:[%s],topic:[%s],processor:[%s]", groupId, topic, processor.getName()));
                } catch (ClassNotFoundException e) {
                    throw new KafkaException(String.format("kafka groupId:[%s],topic:[%s],processor:[%s] processor not found.", groupId, topic, processorElement.getClassName()), e);
                } catch (ClassCastException e) {
                    throw new KafkaException(String.format("kafka groupId:[%s],topic:[%s],processor:[%s] processor is not allowed processor.", groupId, topic, processorElement.getClassName()), e);
                } catch (InstantiationException e) {
                    throw new KafkaException(e);
                } catch (IllegalAccessException e) {
                    throw new KafkaException(e);
                }
            }
        }
        return messageProcessorMap;
    }

    /**
     * Consumer processor线程类
     */
    private static final class ConsumerProcessorRunnable implements Runnable {

        private String name;
        private String topic;
        private KafkaStream stream;
        private IKafkaMessageProcessor messageProcessor;

        private ConsumerProcessorRunnable(String name, String topic, KafkaStream stream, IKafkaMessageProcessor messageProcessor) {
            this.name = name;
            this.topic = topic;
            this.stream = stream;
            this.messageProcessor = messageProcessor;
        }

        @Override
        public void run() {
            LOGGER.info("Process message start... consumer {}, topic {}", name, topic);
            ConsumerIterator iterator = stream.iterator();
            while (iterator.hasNext()) {
                try {
                    MessageAndMetadata data = iterator.next();
                    if (data == null) {
                        continue;
                    }
                    if (!messageProcessor.process(data.topic(), data.message())) {
                        LOGGER.warn("Process message error from kafka consumer {}, topic {},data [{}]", name, data.topic(), data.message());
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            LOGGER.info("Process message stop. consumer {}, topic {}", name, topic);
        }
    }

}
