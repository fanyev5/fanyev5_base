package cn.com.fanyev5.baseservice.redis.client.impl;

import cn.com.fanyev5.baseservice.redis.client.IRedis;
import cn.com.fanyev5.baseservice.redis.exception.RedisException;
import cn.com.fanyev5.baseservice.redis.message.MessageSub;
import cn.com.fanyev5.basecommons.codec.HessianUtil;
import cn.com.fanyev5.basecommons.constant.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Redis客户端实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-6-24
 */
public class RedisImpl implements IRedis {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisImpl.class);

    /**
     * 基于shard的jedis客户端池
     */
    private ShardedJedisPool pool;

    /**
     * PubSub列表
     */
    private List<JedisPubSub> pubSubs = new ArrayList<JedisPubSub>();

    public RedisImpl(ShardedJedisPool pool) {
        this.pool = pool;
    }

    @Override
    public <T extends Serializable> void setObject(String key, final T value, final int expireSecond) {
        ShardedJedis shardedJedis = pool.getResource();
        final byte[] keyBytes = getStringBytes(key);
        try {
            final byte[] valueBytes = HessianUtil.encode(value);
            if (expireSecond > 0) {
                shardedJedis.setex(keyBytes, expireSecond, valueBytes);
            } else {
                // 不设置过期时间
                shardedJedis.set(keyBytes, valueBytes);
            }
        } catch (Exception e) {
            String shardInfo = shardedJedis.getShardInfo(key).toString();
            returnBrokenResource(shardedJedis);
            shardedJedis = null;
            throw new RedisException(shardInfo, e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    @Override
    public Object getObject(String key) {
        ShardedJedis shardedJedis = pool.getResource();
        final byte[] keyBytes = getStringBytes(key);
        try {
            byte[] bytes = shardedJedis.get(keyBytes);
            if (bytes != null) {
                return HessianUtil.decode(bytes);
            }
        } catch (Exception e) {
            String shardInfo = shardedJedis.getShardInfo(key).toString();
            returnBrokenResource(shardedJedis);
            shardedJedis = null;
            throw new RedisException(shardInfo, e);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    @Override
    public Long del(String key) {
        ShardedJedis shardedJedis = pool.getResource();
        try {
            return shardedJedis.del(key);
        } catch (Exception e) {
            String shardInfo = shardedJedis.getShardInfo(key).toString();
            returnBrokenResource(shardedJedis);
            throw new JedisException(shardInfo, e);
        }
    }

    @Override
    public void publish(String key, String msg) {
        ShardedJedis shardedJedis = pool.getResource();
        try {
            shardedJedis.getShard(key).publish(key, msg);
        } catch (Exception e) {
            String shardInfo = shardedJedis.getShardInfo(key).toString();
            returnBrokenResource(shardedJedis);
            shardedJedis = null;
            throw new RedisException(shardInfo, e);
        } finally {
            returnResource(shardedJedis);
        }
    }

    @Override
    public void subscribe(final String key, final MessageSub sub) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShardedJedis shardedJedis = pool.getResource();
                try {
                    pubSubs.add(sub);
                    shardedJedis.getShard(key).subscribe(sub, key);
                } catch (Exception e) {
                    String shardInfo = shardedJedis.getShardInfo(key).toString();
                    returnBrokenResource(shardedJedis);
                    shardedJedis = null;
                    throw new RedisException(shardInfo, e);
                } finally {
                    returnResource(shardedJedis);
                }
            }
        }).start();
    }

    @Override
    public void destroy() {
        for (JedisPubSub pubSub : pubSubs) {
            try {
                pubSub.unsubscribe();
            } catch (Exception e) {
                LOGGER.error("unsubscribe fail.", e);
            }
        }
    }

    /**
     * 按照{@link cn.com.trends.basecommons.constant.Constants#DEFAULT_CHARSET}编码取得byte数组
     *
     * @param str
     * @return
     * @throws RuntimeException
     */
    private byte[] getStringBytes(String str) {
        return str.getBytes(Constants.DEFAULT_CHARSET);
    }

    /**
     * 封装<code>pool.returnBrokenResource</code>,在之前进行is null判断,使其逻辑快速完毕.
     *
     * @param resource
     */
    private void returnBrokenResource(ShardedJedis resource) {
        if (resource != null) {
            pool.returnBrokenResource(resource);
        }
    }

    /**
     * 封装<code>pool.returnResource</code>,在之前进行is null判断,使其逻辑快速完毕.
     *
     * @param resource
     */
    private void returnResource(ShardedJedis resource) {
        if (resource != null) {
            pool.returnResource(resource);
        }
    }

}
