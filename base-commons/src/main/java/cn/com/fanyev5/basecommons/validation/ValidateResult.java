package cn.com.fanyev5.basecommons.validation;

/**
 * 数据验证的返回结果
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public class ValidateResult<T> {

    /**
     * 验证结果，true表示验证通过
     */
    private final boolean success;

    /**
     * 验证失败的错误消息
     */
    private final String errorMsg;

    /**
     * 属性封装类
     */
    private final T t;

    public ValidateResult(boolean success, String errorMsg, T t) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.t = t;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public T getT() {
        return t;
    }

}
