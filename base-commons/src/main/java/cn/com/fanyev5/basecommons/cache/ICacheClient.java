package cn.com.fanyev5.basecommons.cache;

import java.io.Serializable;

/**
 * 缓存客户端接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-09-27
 */
public interface ICacheClient {

    /**
     * 添加对象
     *
     * @param key
     * @param value
     * @return
     */
    <T extends Serializable> void setObject(String key, T value, int expireSeconds);

    /**
     * 查询对象
     *
     * @param key
     * @return
     */
    Object getObject(String key);

    /**
     * 删除Key
     *
     * @param key
     * @return
     */
    Long del(String key);

}
