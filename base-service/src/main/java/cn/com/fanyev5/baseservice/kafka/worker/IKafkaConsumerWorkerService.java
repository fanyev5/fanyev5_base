package cn.com.fanyev5.baseservice.kafka.worker;

import java.util.Set;

/**
 * Kafka Consumer工作服务接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
public interface IKafkaConsumerWorkerService {

    /**
     * 服务启动执行
     *
     * @param consumerNames 使用Consumer名称,对应{@link cn.com.fanyev5.baseservice.kafka.constants.KafkaConstants#KAFKA_CONFIG_FILE_NAME}配置中
     */
    void execStart(Set<String> consumerNames);

    /**
     * 服务停止执行
     */
    void execStop();
}
