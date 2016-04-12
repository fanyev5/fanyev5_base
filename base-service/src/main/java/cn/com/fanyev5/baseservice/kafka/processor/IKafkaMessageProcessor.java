package cn.com.fanyev5.baseservice.kafka.processor;

/**
 * Kafka消息处理期
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public interface IKafkaMessageProcessor<T> {
    /**
     * 消息处理
     *
     * @param topic Topic
     * @param msg   消息体
     * @return true, 处理成功;false,处理失败
     */
    boolean process(String topic, T msg);

}
