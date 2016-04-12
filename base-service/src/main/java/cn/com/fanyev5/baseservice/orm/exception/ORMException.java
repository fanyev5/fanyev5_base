package cn.com.fanyev5.baseservice.orm.exception;

/**
 * ORM异常类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-23
 */
public class ORMException extends RuntimeException {

    private static final long serialVersionUID = -3546073392919857019L;

    public ORMException() {
        super();
    }

    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }

    public ORMException(Throwable cause) {
        super(cause);
    }
}
