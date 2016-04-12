package cn.com.fanyev5.baseservice.redis.client.impl;

import cn.com.fanyev5.baseservice.base.config.xml.server.ServerGroupConfigs;
import cn.com.fanyev5.baseservice.redis.client.IRedis;
import cn.com.fanyev5.baseservice.redis.exception.RedisException;
import cn.com.fanyev5.basecommons.service.ServiceLoader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;
import java.util.Map;

/**
 * Redis loader实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public class RedisServiceLoaderImpl extends ServiceLoader<IRedis> {

    private final ServerGroupConfigs serverGroupConfigs;

    public RedisServiceLoaderImpl(ServerGroupConfigs serverGroupConfigs) {
        Preconditions.checkNotNull(serverGroupConfigs, "serverGroupConfigs");
        // Server
        Preconditions.checkNotNull(serverGroupConfigs.getServers(), "servers");
        // Group
        Preconditions.checkNotNull(serverGroupConfigs.getGroups(), "groups");

        this.serverGroupConfigs = serverGroupConfigs;
    }

    @Override
    public IRedis load(final String key) throws RedisException {
        ServerGroupConfigs.Group group = serverGroupConfigs.getGroups().get(key);
        Preconditions.checkNotNull(group, "group is NULL");
        List<String> serverList = group.getServers();
        Preconditions.checkNotNull(serverList, "serverList is null");
        Preconditions.checkArgument(serverList.size() > 0, "serverList is empty");

        // 获取Pool配置
        Map<String, String> properties = group.getProperties();
        JedisPoolConfig config = new JedisPoolConfig();
        config.maxActive = Integer.parseInt(properties.get("redis.conf.max.active"));
        config.maxWait = Long.parseLong(properties.get("redis.conf.max.wait"));
        config.maxIdle = Integer.parseInt(properties.get("redis.conf.max.idle"));
        config.minIdle = Integer.parseInt(properties.get("redis.conf.min.idle"));
        int timeout = Integer.parseInt(properties.get("redis.conf.timeout"));

        // 获得key对应的服务器集群列表
        List<JedisShardInfo> shards = Lists.newArrayList();
        for (String serverName : serverList) {
            Preconditions.checkNotNull(serverName);
            ServerGroupConfigs.Server server = serverGroupConfigs.getServers().get(serverName);
            Preconditions.checkNotNull(server, "Can't find the sever for key:%s,server name:%s", key, serverName);
            JedisShardInfo jedisShardInfo = new JedisShardInfo(
                    server.getHost(), Integer.parseInt(server.getPort()), timeout, server.getName());
            shards.add(jedisShardInfo);
        }

        ShardedJedisPool pool = new ShardedJedisPool(config, shards);
        return new RedisImpl(pool);
    }

}
