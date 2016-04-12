package cn.com.fanyev5.basecommons.service;

/**
 * Service异常类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -2939042219457654152L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
