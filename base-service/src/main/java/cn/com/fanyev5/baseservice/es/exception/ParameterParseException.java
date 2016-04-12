package cn.com.fanyev5.baseservice.es.exception;

/**
 * 参数解析异常
 *
 * @author fanqi427@gmail.com
 * @since 2013-12-19
 */
public class ParameterParseException extends IllegalArgumentException {

    private static final long serialVersionUID = 1970177548474571822L;

    public ParameterParseException() {
        super();
    }

    public ParameterParseException(String message) {
        super(message);
    }

    public ParameterParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterParseException(Throwable cause) {
        super(cause);
    }

}
