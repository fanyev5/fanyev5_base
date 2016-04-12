package cn.com.fanyev5.baseservice.kafka.worker;

import java.util.Set;

/**
 * Kafka producer工作服务接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public interface IKafkaProducerWorkerService {

    /**
     * 服务启动执行
     *
     * @param producerNames 使用Producer名称,对应{@link cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants#KAFKA_CONFIG_FILE_NAME}配置中
     */
    void execStart(Set<String> producerNames);

    /**
     * 服务停止执行
     */
    void execStop();
}
