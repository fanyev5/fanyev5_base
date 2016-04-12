package cn.com.fanyev5.baseservice.es.exception;

/**
 * ES异常
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-19
 */
public class ESException extends RuntimeException {

    private static final long serialVersionUID = 2139121598747232658L;

    public ESException() {
        super();
    }

    public ESException(String message) {
        super(message);
    }

    public ESException(String message, Throwable cause) {
        super(message, cause);
    }

    public ESException(Throwable cause) {
        super(cause);
    }

}
