package cn.com.fanyev5.basecommons.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

/**
 * 通用命名服务
 * <p>
 * 依赖: Guava {@link com.google.common.cache.LoadingCache}
 * </p>
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public class CommonNamedResourceService<T> implements INameResourceService<T> {

    private final LoadingCache<String, T> clientCache;

    public CommonNamedResourceService(ServiceLoader<T> cacheLoader) {
        this.clientCache = CacheBuilder.newBuilder().build(cacheLoader);
    }

    public CommonNamedResourceService(ServiceLoader<T> cacheLoader, RemovalListener<String, T> removalListener) {
        this.clientCache = CacheBuilder.newBuilder().removalListener(removalListener).build(cacheLoader);
    }

    @Override
    public T get(String name) throws ServiceException {
        try {
            return clientCache.get(name);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void destroy() {
        clientCache.invalidateAll();
    }

}