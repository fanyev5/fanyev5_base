package cn.com.fanyev5.basecommons.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * SpringContext管理
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
@Component
public class SpringContextManager {

    private static ApplicationContext appContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }

    /**
     * 获取Bean
     */
    public static <T> T getBean(String beanName) {
        return (T) appContext.getBean(beanName);
    }

    /**
     * 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return appContext.getBean(clazz);
    }

    /**
     * 获取Bean
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return appContext.getBean(beanName, clazz);
    }

}
