package cn.com.fanyev5.basecommons.service;

import com.google.common.cache.CacheLoader;

/**
 * 服务Loader抽象实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public abstract class ServiceLoader<T> extends CacheLoader<String, T> {

}
