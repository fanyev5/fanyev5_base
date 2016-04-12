package cn.com.fanyev5.baseservice.kafka.message.impl;

import com.google.common.base.Charsets;

import cn.com.fanyev5.baseservice.kafka.message.IKafkaMessageSerializer;
import kafka.message.Message;

import java.nio.ByteBuffer;

/**
 * Kafka字符串序列化
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class StringMessageSerializer implements IKafkaMessageSerializer<String> {

    @Override
    public String toEvent(Message message) {
        if (message == null) {
            return null;
        }
        ByteBuffer buffer = message.payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, Charsets.UTF_8);
    }

    @Override
    public Message toMessage(String event) {
        if (event == null) {
            return null;
        }
        return new Message(event.getBytes(Charsets.UTF_8));
    }
}
