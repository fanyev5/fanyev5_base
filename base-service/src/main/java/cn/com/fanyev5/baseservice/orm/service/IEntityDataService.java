package cn.com.fanyev5.baseservice.orm.service;

import cn.com.fanyev5.baseservice.orm.entity.IEntity;

/**
 * 对Entity数据基础缓存Service接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-29
 */
public interface IEntityDataService<T extends IEntity> {

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    public T save(T entity);

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    public boolean update(T entity);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public boolean delete(long id);

    /**
     * 取得
     *
     * @param id
     * @return
     */
    public T get(long id);


}
