package cn.com.fanyev5.basecommons.life.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cn.com.fanyev5.basecommons.life.ILifeService;

/**
 * 生命周期服务抽象接口,基于Spring
 *
 * @author fanqi427@gmail.com
 * @since 2013-7-4
 */
public abstract class AbstractSpringLifeService implements ILifeService, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringLifeService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            start();
        } catch (Exception e) {
            LOGGER.error("Life service from start() fail.", e);
            throw e;
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            stop();
        } catch (Exception e) {
            LOGGER.error("Life service from stop() fail.", e);
            throw e;
        }
    }

    @Override
    public abstract void start();

    @Override
    public abstract void stop();
}
