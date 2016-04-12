package cn.com.trends.baseservice.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.com.fanyev5.baseservice.orm.entity.IEntity;

/**
 * Test2 Entity
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-06
 */
@Entity
@Table(name = "t_test2")
public class Test2Entity implements IEntity {

    private static final long serialVersionUID = 6456640560647132500L;

    @Id
    @Column(name = "tid")
    private long tid;
    @Column(name = "field_1")
    private String field1;
    @Column(name = "field_2")
    private String field2;
    @Column(name = "field_3")
    private String field3;

    @Override
    public long getId() {
        return tid;
    }

    @Override
    public void setId(long id) {
        this.tid = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }
}
