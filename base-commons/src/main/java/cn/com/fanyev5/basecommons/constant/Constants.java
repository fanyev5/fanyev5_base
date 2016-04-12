package cn.com.fanyev5.basecommons.constant;

import java.nio.charset.Charset;

/**
 * 基础常量
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class Constants {

    private Constants() {
    }

    /**
     * 全局默认编码
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 全局默认编码
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);

    /**
     * 换行符
     */
    public static final char NEW_LINE = '\n';

}
