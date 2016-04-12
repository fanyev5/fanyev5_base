package cn.com.fanyev5.baseservice.orm.entity;

import com.google.common.base.Preconditions;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 自动生成ID Entity基类
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public abstract class AutoIdEntity implements IEntity {

    private static final long serialVersionUID = -6101230291509851384L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public final long getId() {
        return id;
    }

    public final void setId(long id) {
        Preconditions.checkArgument(id > 0, "id");
        this.id = id;
    }

}
