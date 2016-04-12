package cn.com.fanyev5.baseservice.orm.util;

import cn.com.fanyev5.baseservice.orm.exception.ORMException;
import cn.com.fanyev5.baseservice.redis.client.IRedis;
import cn.com.fanyev5.basecommons.service.INameResourceService;

/**
 * Cache工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-29
 */
public final class CacheUtil {

    private CacheUtil() {
    }

    /**
     * 生成key
     *
     * @param key
     * @param values
     * @return
     */
    public static String genCacheKey(String key, Object... values) {
        return String.format(key, values);
    }

    /**
     * 获得指定名字的IRedis : 这里如果发现缓存服务不存在，暂时直接抛出异常，不允许访问未定义的缓存服务
     *
     * @param name
     * @return
     */
    public static IRedis getRedisCacheClient(INameResourceService<IRedis> cacheService, String name) {
        try {
            IRedis redis = cacheService.get(name);
            if (redis == null) {
                throw new IllegalArgumentException(String.format("unkown cache service name like %s", name));
            }
            return redis;
        } catch (Exception e) {
            throw new ORMException("Cache Client get exception. name :" + name, e);
        }
    }
}
