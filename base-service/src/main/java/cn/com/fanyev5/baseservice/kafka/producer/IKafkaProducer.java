package cn.com.fanyev5.baseservice.kafka.producer;

import java.util.List;

/**
 * Kafka producer接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public interface IKafkaProducer<T> {

    /**
     * 获取Name
     *
     * @return
     */
    String getName();

    /**
     * 发送单个数据
     *
     * @param topic
     * @param data
     */
    void send(String topic, T data);

    /**
     * 发送数据集
     *
     * @param topic
     * @param datas
     */
    void send(String topic, List<T> datas);

    /**
     * 销毁
     */
    void destroy();

}
