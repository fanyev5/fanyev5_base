package cn.com.fanyev5.baseservice.orm.dao;

import java.util.List;

import cn.com.fanyev5.baseservice.orm.entity.IEntity;

/**
 * Dao基础接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public interface IDao<T extends IEntity> {

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    T save(T entity);

    /**
     * 批量保存
     *
     * @param entities
     * @return
     */
    List<T> save(List<T> entities);

    /**
     * 删除
     *
     * @param entity
     * @return
     */
    boolean delete(T entity);

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    boolean update(T entity);

    /**
     * 获取
     *
     * @param id
     * @param clazz
     * @return
     */
    T get(long id, Class<T> clazz);
}
