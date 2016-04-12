package cn.com.fanyev5.basecommons.util;


import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * UUID工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class UUIDUtil {

    private UUIDUtil() {
    }

    /**
     * 原始UUID的连接符
     */
    private static final String STR_LINK = "-";

    /**
     * 被替换成空字符串
     */
    private static final String STR_NULL = "";

    /**
     * 生成原始带分隔符的UUID
     *
     * @return
     */
    public static String genOriginUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成去除分隔符的UUID
     *
     * @return
     */
    public static String genTerseUuid() {
        return StringUtils.replace(genOriginUuid(), STR_LINK, STR_NULL);
    }

}