package cn.com.fanyev5.basecommons.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;


/**
 * 日期处理工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-18
 */
public final class DateUtil {

    public static final String DATETIME_STR = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    /**
     * 格式化当前日期 格式: <code>yyyy-MM-dd</code>
     *
     * @return
     */
    public static String formatDate(Date date) {
        return DateFormatUtils.ISO_DATE_FORMAT.format(date);
    }

    /**
     * 格式化当前日期
     *
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 解析日期
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        try {
            return DateUtils.parseDate(dateStr, new String[]{pattern});
        } catch (ParseException e) {
            throw new RuntimeException("parse date exception.", e);
        }
    }

}
