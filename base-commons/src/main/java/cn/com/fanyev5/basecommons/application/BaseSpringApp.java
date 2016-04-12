package cn.com.fanyev5.basecommons.application;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * 基础Spring Application
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-30
 */
public abstract class BaseSpringApp {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseSpringApp.class);

    protected final String[] args;

    protected final ClassPathXmlApplicationContext appContext;

    public BaseSpringApp(String[] args, String locations) {
        this.args = (String[]) ArrayUtils.clone(args);
        LOGGER.info("args: " + Arrays.toString(this.args));

        this.appContext = new ClassPathXmlApplicationContext(locations);

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    shutdown();
                } catch (Exception e) {
                    LOGGER.error("Exec shutdown exception.", e);
                    // Ignore
                }

                try {
                    LOGGER.info("App close.");
                    if (appContext != null) {
                        appContext.close();
                    }
                } catch (Exception e) {
                    LOGGER.error("App close exception.", e);
                }
            }
        }));
    }

    protected void shutdown() {
        // Ignore
    }

}
