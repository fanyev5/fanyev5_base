package cn.com.fanyev5.basecommons.service;

/**
 * 基于命名方式提供的基础服务接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-25
 */
public interface INameResourceService<T> {

    /**
     * 根据名字获取指定服务
     *
     * @param name
     * @return
     * @throws Exception
     */
    T get(String name) throws ServiceException;

    /**
     * 销毁
     */
    void destroy() throws ServiceException;
}
