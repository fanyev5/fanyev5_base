package cn.com.fanyev5.baseservice.orm.entity;

import java.io.Serializable;

/**
 * IEntity
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public interface IEntity extends Serializable {

    long getId();

    void setId(long id);
}
