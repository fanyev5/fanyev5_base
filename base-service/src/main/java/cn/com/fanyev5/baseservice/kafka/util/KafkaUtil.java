package cn.com.fanyev5.baseservice.kafka.util;

import cn.com.fanyev5.baseservice.kafka.constants.ConsumerConstants;
import cn.com.fanyev5.basecommons.codec.HessianUtil;

import com.google.common.base.Charsets;
import kafka.consumer.ConsumerConfig;
import kafka.producer.ProducerConfig;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Properties;

/**
 * Kafka工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public final class KafkaUtil {

    private KafkaUtil() {
    }

    /**
     * 构建kafka的消费者配置
     *
     * @param configMap
     * @return
     */
    public static ConsumerConfig createConsumerConfig(Map<String, String> configMap) {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        return new ConsumerConfig(props);
    }

    /**
     * 构建kafka的生产者配置
     *
     * @param configMap
     * @return
     */
    public static ProducerConfig createProducerConfig(Map<String, String> configMap) {
        Properties props = new Properties();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }
        return new ProducerConfig(props);
    }

    /**
     * 处理config的value值,把,换成"<#@#>"
     *
     * @param value
     * @return
     */
    public static String encodeConfigValue(String value) {
        return StringUtils.replace(value, ",", "<#@#>");
    }

    /**
     * 处理event.handler接收到的参数,把它的"<#@#>"号换为,号
     *
     * @param value
     * @return
     */
    public static String decodeConfigValue(String value) {
        return StringUtils.replace(value, "<#@#>", ",");
    }

    /**
     * 对象序列化
     *
     * @param event
     * @param <T>
     * @return
     */
    public static <T> byte[] toBytes(T event) {
        if (event == null) {
            return new byte[0];
        }
        return HessianUtil.encode(event);
    }

    /**
     * 对象反序列化
     *
     * @param bytes
     * @param <T>
     * @return
     */
    public static <T> T fromByte(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return (T) HessianUtil.decode(bytes);
    }

    /**
     * 将对象Base64, 使用Hessian序列化
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String toBase64(T data) {
        byte[] bytes = toBytes(data);
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 还原对象
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> T fromBase64(String data) {
        byte[] bytes = Base64.decodeBase64(data.getBytes(Charsets.UTF_8));
        return fromByte(bytes);
    }

    /**
     * 生成消息处理实现Key
     *
     * @param groupId
     * @param topic
     * @return
     */
    public static String genMessageProcessorKey(String groupId, String topic) {
        return String.format(ConsumerConstants.GROUP_TOPIC_FORMAT, groupId, topic);
    }

}
