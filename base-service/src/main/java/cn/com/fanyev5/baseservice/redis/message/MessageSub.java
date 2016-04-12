package cn.com.fanyev5.baseservice.redis.message;

import com.google.common.base.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * 消息订阅, 基于Redis
 * 
 * @author fanqi427@gmail.com
 * @since 2013-6-25
 */
public class MessageSub extends JedisPubSub {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSub.class);

    private Function<String, Void> fun;

    public MessageSub(Function<String, Void> fun) {
        this.fun = fun;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            fun.apply(message);
        } catch (Exception e) {
            LOGGER.error("Message exception.", e);
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        // ignore
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        // ignore
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // ignore
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        // ignore
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        // ignore
    }

}
