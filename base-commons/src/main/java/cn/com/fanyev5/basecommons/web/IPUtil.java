package cn.com.fanyev5.basecommons.web;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * IP处理工具类
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class IPUtil {

    private IPUtil() {
    }

    /**
     * 取得当前用户IP
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        // 根据优先级规则，如果取得一个有效ip，立即返回
        String ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    /**
     * 是否是有效的ip地址
     *
     * @param ip
     * @return
     */
    private static boolean isValidIp(String ip) {
        return (!Strings.isNullOrEmpty(ip)) && (!"unknown".equalsIgnoreCase(ip));
    }

}
