package cn.com.fanyev5.basecommons.validation;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 广告相关请求参数检查结果
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public class CheckResult {

    /**
     * 参数错误类型
     */
    public enum ParamsError {
        /**
         * 必须
         */
        REQUIRED,
        /**
         * 格式
         */
        UNFORMAT,
        /**
         * 其他
         */
        OTHERERR;
    }

    /**
     * 校验是否通过
     */
    private boolean passed;

    /**
     * 错误信息
     */
    private StringBuilder errorMsg;

    /**
     * 记录请求参数键值对
     */
    private Map<String, Object> paramKVs;

    public CheckResult() {
        passed = true;
        errorMsg = new StringBuilder();
        paramKVs = Maps.newHashMap();
    }

    /**
     * 是否通过了校验
     * 
     * @return
     */
    public boolean isPassed() {
        return passed;
    }

    /**
     * 取得错误信息
     * 
     * @return
     */
    public String getErrorMsg() {
        return errorMsg.toString();
    }

    /**
     * 添加错误信息
     * 
     * @param paraName
     *            参数名
     * @param error
     *            参数错误类型
     */
    public void appendErrorMsg(String paraName, ParamsError error) {
        // 设置验证错误
        if (passed) {
            this.passed = false;
        }
        // 设置错误信息
        errorMsg.append(paraName).append(":");
        if (error != null) {
            errorMsg.append(error.name().toLowerCase());
        } else {
            errorMsg.append(ParamsError.OTHERERR.name().toLowerCase());
        }
        errorMsg.append(";");
    }

    /**
     * 添加请求参数键值对
     * 
     * @param paraName
     *            参数名
     * @param paraValue
     *            参数值
     */
    public void appendParam(String paraName, Object paraValue) {
        if (paraValue != null) {
            paramKVs.put(paraName, paraValue);
        }
    }

    /**
     * 取得请求参数键值对Map
     * 
     * @return
     */
    public Map<String, Object> getParams() {
        return paramKVs;
    }

}
