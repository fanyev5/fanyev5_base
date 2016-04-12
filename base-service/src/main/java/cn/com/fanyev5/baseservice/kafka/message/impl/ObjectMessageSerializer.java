package cn.com.fanyev5.baseservice.kafka.message.impl;

import kafka.message.Message;

import java.nio.ByteBuffer;

import cn.com.fanyev5.baseservice.kafka.message.IKafkaMessageSerializer;
import cn.com.fanyev5.baseservice.kafka.util.KafkaUtil;

/**
 * Kafka对象序列化
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class ObjectMessageSerializer<T> implements IKafkaMessageSerializer<T> {

    @Override
    public T toEvent(Message message) {
        if (message == null) {
            return null;
        }
        ByteBuffer buffer = message.payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, bytes.length);
        return KafkaUtil.fromByte(bytes);
    }

    @Override
    public Message toMessage(T event) {
        if (event == null) {
            return null;
        }
        byte[] bytes = KafkaUtil.toBytes(event);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new Message(bytes);
    }
}
