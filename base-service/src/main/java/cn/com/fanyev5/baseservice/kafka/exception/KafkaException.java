package cn.com.fanyev5.baseservice.kafka.exception;

/**
 * Kafka异常类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class KafkaException extends RuntimeException {

    private static final long serialVersionUID = 2036879841508713524L;

    public KafkaException() {
        super();
    }

    public KafkaException(String message) {
        super(message);
    }

    public KafkaException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaException(Throwable cause) {
        super(cause);
    }
}
