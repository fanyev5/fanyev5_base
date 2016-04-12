package cn.com.fanyev5.baseservice.kafka.message;

import kafka.serializer.Decoder;
import kafka.serializer.Encoder;

/**
 * Kafka producer消息序列化接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public interface IKafkaMessageSerializer<T> extends Encoder<T>, Decoder<T> {

}
