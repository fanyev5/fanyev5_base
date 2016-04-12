package cn.com.fanyev5.baseservice.kafka.consumer;

import kafka.consumer.KafkaStream;

import java.util.List;
import java.util.Map;

/**
 * Kafka consumer接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public interface IKafkaConsumer<T> {


    /**
     * 取得kafka的consumer的stream,key是topic
     *
     * @return
     */
    public Map<String, List<KafkaStream<T>>> getMessageStreams();

    /**
     * 销毁
     */
    public void destroy();
}
