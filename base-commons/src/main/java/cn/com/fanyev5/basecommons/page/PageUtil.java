package cn.com.fanyev5.basecommons.page;

import javax.servlet.http.HttpServletRequest;

/**
 * Page工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-10-22
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * 构建Page
     *
     * @param request
     * @param <T>
     * @return
     */
    public static <T> Page<T> buildPage(HttpServletRequest request) {
        try {
            int pIndex = Integer.parseInt(request.getParameter("p"));
            int pSize = Integer.parseInt(request.getParameter("n"));
            return new Page<T>(pIndex, pSize);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
