package cn.com.fanyev5.basecommons.web;

import com.google.common.base.Strings;

import cn.com.fanyev5.basecommons.validation.CheckResult;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Request工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    /**
     * 格式化请求参数，封装为<code>Map[String, String]</code>
     * <p>
     * 说明: 去除参数中<code>\r</code>,<code>\n</code>,<code>\r\n</code>
     * </p>
     *
     * @param request
     * @return
     */
    public static Map<String, String> formatParamsMap(HttpServletRequest request) {
        Map<String, String[]> originMap = request.getParameterMap();
        int count = originMap.size();
        Map<String, String> formatMap = new HashMap<String, String>(count);
        for (Map.Entry<String, String[]> entry : originMap.entrySet()) {
            String[] vals = entry.getValue();
            if (vals == null || vals.length == 0) {
                formatMap.put(entry.getKey(), null);
            } else {
                formatMap.put(entry.getKey(), StringUtils.trim(StringUtils.replaceChars(vals[0], "\r\n", null)));
            }
        }
        return formatMap;
    }

    /**
     * 拼装参数字符串
     *
     * @param paramsMap 请求参数Map
     * @return 字符串
     */
    public static String getParamString(Map<String, Object> paramsMap) {
        if (paramsMap == null || paramsMap.size() == 0) {
            return StringUtils.EMPTY;
        }
        StringBuilder strBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
            strBuilder.append(entry.getKey()).append(':');
            String value = (entry.getValue() == null) ? StringUtils.EMPTY : entry.getValue().toString();
            strBuilder.append(value).append(';');
        }
        return strBuilder.toString();
    }

    /**
     * 拼装原始请求的parameter字符串
     *
     * @param request
     * @return
     */
    public static String getParamString(HttpServletRequest request) {
        StringBuilder strBuilder = new StringBuilder();
        Map<String, String[]> originMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : originMap.entrySet()) {
            // key
            strBuilder.append(entry.getKey()).append(':');
            // value
            String[] vals = entry.getValue();
            if (vals == null || vals.length == 0) {
                strBuilder.append(StringUtils.EMPTY);
            } else {
                strBuilder.append(vals[0]);
            }
            strBuilder.append(';');
        }
        return strBuilder.toString();
    }

    /**
     * 获取请求参数的值, 带验证
     *
     * @param paramName   参数属性名
     * @param pattern     格式验证正则
     * @param clazz       类型
     * @param map         参数map
     * @param checkResult 传入参数验证
     * @return
     */
    public static <T> T get(String paramName, Pattern pattern, Class<T> clazz, Map<String, String> map,
                            CheckResult checkResult) {
        T t = null;
        String paramVal = StringUtils.trimToNull(map.get(paramName));
        // 为空判断
        if (Strings.isNullOrEmpty(paramVal)) {
            checkResult.appendErrorMsg(paramName, CheckResult.ParamsError.REQUIRED);
        } else if (null == pattern || pattern.matcher(paramVal).matches()) {
            try {
                t = convertToType(clazz, paramVal);
            } catch (Exception e) {
                checkResult.appendErrorMsg(paramName, CheckResult.ParamsError.UNFORMAT);
            }
        } else {
            checkResult.appendErrorMsg(paramName, CheckResult.ParamsError.UNFORMAT);
        }
        checkResult.appendParam(paramName, t);
        return t;
    }

    /**
     * 获取请求参数的值, 可为空, 带默认值
     *
     * @param paramName   参数属性名
     * @param pattern     格式验证正则
     * @param clazz       类型
     * @param map         参数map
     * @param checkResult 传入参数验证
     * @param obj         默认值
     * @return
     */
    public static <T> T get(String paramName, Pattern pattern, Class<T> clazz, Map<String, String> map,
                            CheckResult checkResult, T obj) {
        T t = null;
        String paramVal = StringUtils.trimToNull(map.get(paramName));
        // 为空判断
        if (Strings.isNullOrEmpty(paramVal)) {
            t = obj;
        } else if (null == pattern || pattern.matcher(paramVal).matches()) {
            try {
                t = convertToType(clazz, paramVal);
            } catch (Exception e) {
                checkResult.appendErrorMsg(paramName, CheckResult.ParamsError.UNFORMAT);
            }
        } else {
            checkResult.appendErrorMsg(paramName, CheckResult.ParamsError.UNFORMAT);
        }
        checkResult.appendParam(paramName, t);
        return t;
    }

    /**
     * 使用指定的字符串值反射生成目标类型的实例
     *
     * @param type  目标类型
     * @param value 指定字符串值
     * @param <T>   目标类型泛型
     * @return
     * @throws Exception 如果生成实例失败抛出异常
     */
    private static <T> T convertToType(Class<T> type, String value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (String.class == type) {
            return (T) value;
        }
        Constructor<T> constructor = type.getConstructor(String.class);
        return constructor.newInstance(value);
    }

}
