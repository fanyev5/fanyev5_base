package cn.com.fanyev5.basecommons.web;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 性能监控Probe Filter
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-7
 */
//TODO: 待完成
public class ProbeFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProbeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        // Ignore
    }

    @Override
    public void destroy() {
        // Ignore
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        String originThreadName = null;
        final String uri;

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            uri = httpRequest.getRequestURI();
            if (uri != null) {
                // 更新线程名用以log输出请求URI
                originThreadName = updateThreadName(httpRequest);
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error("Uncaught Exception", e);
            throw new ServletException(e);
        } finally {
            if (originThreadName != null) {
                Thread.currentThread().setName(originThreadName);
            }
        }
    }

    /**
     * 使用原始请求URI,更新当前线程名,提供给log4j的pattern的%t参数
     * 
     * @param httpRequest
     * @return 返回更新前的原始线程名，用以还原原始线程名
     */
    private String updateThreadName(HttpServletRequest httpRequest) {
        String originThreadName = Thread.currentThread().getName();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(originThreadName);
        strBuilder.append(" - ");
        strBuilder.append(httpRequest.getRequestURI());
        String queryString = httpRequest.getQueryString();
        if (!Strings.isNullOrEmpty(queryString)) {
            strBuilder.append("?");
            strBuilder.append(queryString);
        }
        Thread.currentThread().setName(strBuilder.toString());
        return originThreadName;
    }

}
