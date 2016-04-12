package cn.com.fanyev5.basecommons.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * Bean工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-10-22
 */
public final class BeanUtil {

    private BeanUtil() {

    }

    /**
     * Copy属性
     *
     * @param dest 目标实例
     * @param orig 原数据实例
     */
    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Bean copy properties exception.", e);
        }
    }

    /**
     * 打印
     *
     * @param beanClass
     */
    public static void printRowMapping(Class beanClass) {
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(String.format("bean.set%s(rs.get%s(\"%s\"));"
                    , StringUtils.capitalize(field.getName())
                    , StringUtils.capitalize(field.getType().getSimpleName())
                    , field.getName()));
        }
    }
}
