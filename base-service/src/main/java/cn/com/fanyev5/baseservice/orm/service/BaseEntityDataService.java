package cn.com.fanyev5.baseservice.orm.service;

import cn.com.fanyev5.baseservice.orm.dao.IDao;
import cn.com.fanyev5.baseservice.orm.entity.IEntity;
import cn.com.fanyev5.baseservice.orm.util.CacheUtil;
import cn.com.fanyev5.basecommons.cache.ICacheClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;

/**
 * 对Entity数据基础缓存Service
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-29
 */
public abstract class BaseEntityDataService<T extends IEntity> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseEntityDataService.class);

    /**
     * 缓存开关
     */
    private boolean cacheSwitch = false;
    /**
     * entity cache key
     */
    private final String cacheKey;
    /**
     * 缓存默认时间
     */
    private final int cacheTime;

    /**
     * 泛性的具体类型Class
     */
    private Class<T> entityClass;

    /**
     * 取得当前注入dal的抽象类
     */
    protected abstract IDao<T> getDao();

    /**
     * 获得关联子key
     */
    protected abstract String[] getSubKeys(T entity);

    /**
     * 获得缓存客户端
     *
     * @return
     */
    protected abstract ICacheClient getCacheClient();

    public BaseEntityDataService(boolean cacheSwitch, String cacheKey, int cacheTime) {
        this.cacheSwitch = cacheSwitch;
        this.cacheKey = cacheKey;
        this.cacheTime = cacheTime;

        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 保存 (DB + Cache)
     *
     * @param entity
     * @return
     */
    public T save(T entity) {
        T addTr = getDao().save(entity);
        if (null != addTr && addTr.getId() > 0) {
            this.setToCache(entity);
            return addTr;
        }
        return null;
    }

    /**
     * 获取 (DB + Cache)
     *
     * @param id ID
     * @return
     */
    public T get(long id) {
        T t = this.getFromCache(id);
        if (t == null) {
            t = getDao().get(id, this.entityClass);
            if (t != null && t.getId() > 0) {
                this.setToCache(t);
            }
        }
        return t;
    }

    /**
     * 更新 (DB + Cache)
     *
     * @param entity
     * @return
     */
    public boolean update(T entity) {
        boolean isSuccess = getDao().update(entity);
        if (isSuccess) {
            this.setToCache(entity);
        }
        return isSuccess;
    }

    /**
     * 删除 (DB + Cache)
     *
     * @param entity
     * @return
     */
    public boolean delete(T entity) {
        boolean isSuccess = getDao().delete(entity);
        if (isSuccess) {
            this.delFromCache(entity);
        }
        return isSuccess;
    }

    /**
     * 删除 (DB + Cache)
     *
     * @param id ID
     * @return
     */
    public boolean delete(long id) {
        T entity = this.get(id);
        if (entity != null) {
            return this.delete(entity);
        }
        return false;
    }

    /**
     * 存储对象(Cache)
     *
     * @param entity
     */
    protected void setToCache(T entity) {
        if (cacheSwitch) {
            ICacheClient msgEntityCache = this.getCacheClient();
            if (msgEntityCache != null) {
                String key = CacheUtil.genCacheKey(this.cacheKey, String.valueOf(entity.getId()));
                msgEntityCache.setObject(key, entity, this.cacheTime);
                String[] subKeys = this.getSubKeys(entity);
                if (subKeys != null) {
                    for (String subKey : this.getSubKeys(entity)) {
                        msgEntityCache.setObject(subKey, key, this.cacheTime);
                    }
                }
            }
        }
    }

    /**
     * 获取对象(Cache)
     *
     * @param id ID
     * @return
     */
    protected T getFromCache(long id) {
        if (cacheSwitch) {
            String key = CacheUtil.genCacheKey(this.cacheKey, String.valueOf(id));
            return (T) this.getCacheClient().getObject(key);
        }
        return null;
    }

    /**
     * 获取对象(Cache)
     *
     * @param subKey 缓存子Key
     * @return
     */
    protected T getFromCacheBySubKey(String subKey) {
        if (cacheSwitch) {
            ICacheClient cacheClient = this.getCacheClient();
            Object key = cacheClient.getObject(subKey);
            if (key != null) {
                return (T) cacheClient.getObject(key.toString());
            }
        }
        return null;
    }

    /**
     * 删除对象(cache)
     *
     * @param entity
     */
    protected void delFromCache(T entity) {
        if (cacheSwitch) {
            String key = CacheUtil.genCacheKey(this.cacheKey, String.valueOf(entity.getId()));
            String[] subKeys = this.getSubKeys(entity);
            if (subKeys != null && subKeys.length > 0) {
                ICacheClient redis = this.getCacheClient();
                redis.del(key);
                for (String subKey : subKeys) {
                    redis.del(subKey);
                }
            } else {
                this.getCacheClient().del(key);
            }
        }
    }

}
