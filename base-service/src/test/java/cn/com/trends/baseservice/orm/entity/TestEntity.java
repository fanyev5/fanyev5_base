package cn.com.trends.baseservice.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.com.fanyev5.baseservice.orm.entity.AutoIdEntity;

/**
 * Test Entity
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-06
 */
@Entity
@Table(name = "t_test")
public class TestEntity extends AutoIdEntity {

    private static final long serialVersionUID = 6456640560647132500L;

    @Column
    private String field1;
    @Column
    private String field2;
    @Column
    private String field3;

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
