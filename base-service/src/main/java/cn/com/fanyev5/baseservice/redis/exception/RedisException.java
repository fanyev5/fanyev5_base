package cn.com.fanyev5.baseservice.redis.exception;

/**
 * Redis异常类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class RedisException extends RuntimeException {

    private static final long serialVersionUID = 2299397201311612695L;

    public RedisException() {
        super();
    }

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}
