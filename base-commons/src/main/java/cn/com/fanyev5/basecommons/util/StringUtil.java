package cn.com.fanyev5.basecommons.util;

/**
 * 字符串工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-06
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String toUpperByFirstChar(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String toLowerByFirstChar(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

}
