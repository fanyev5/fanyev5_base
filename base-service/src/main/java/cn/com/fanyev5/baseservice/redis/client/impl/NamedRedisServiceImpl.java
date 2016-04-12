package cn.com.fanyev5.baseservice.redis.client.impl;

import cn.com.fanyev5.baseservice.base.config.xml.server.ServerGroupConfigs;
import cn.com.fanyev5.baseservice.redis.client.INamedRedisService;
import cn.com.fanyev5.baseservice.redis.client.IRedis;
import cn.com.fanyev5.baseservice.redis.constants.RedisConstants;
import cn.com.fanyev5.basecommons.service.CommonNamedResourceService;
import cn.com.fanyev5.basecommons.xml.JAXBUtil;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * Redis命名服务接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public class NamedRedisServiceImpl extends CommonNamedResourceService<IRedis> implements INamedRedisService {

    public NamedRedisServiceImpl() {
        super(new RedisServiceLoaderImpl(
                JAXBUtil.unmarshal(ServerGroupConfigs.class, RedisConstants.REDIS_CONFIG_FILE_NAME))
                , new RemovalListener<String, IRedis>() {
            @Override
            public void onRemoval(RemovalNotification<String, IRedis> notification) {
                if (notification != null) {
                    IRedis redis = notification.getValue();
                    if (redis != null) {
                        redis.destroy();
                    }
                }
            }
        });
    }

}
