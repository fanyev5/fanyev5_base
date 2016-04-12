package cn.com.fanyev5.baseservice.redis.client;

import cn.com.fanyev5.baseservice.redis.message.MessageSub;
import cn.com.fanyev5.basecommons.cache.ICacheClient;

/**
 * Redis客户端接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-24
 */
public interface IRedis extends ICacheClient {

    /**
     * 发布消息
     *
     * @param key
     * @param msg
     */
    void publish(String key, String msg);

    /**
     * 订阅消息
     * <p>
     * ps:临时方法, 此方法长期占有Pool资源
     * </p>
     *
     * @param key
     * @param sub
     */
    void subscribe(String key, MessageSub sub);

    /**
     * 销毁
     */
    void destroy();

}
