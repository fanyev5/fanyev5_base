package cn.com.fanyev5.baseservice.orm.common;

import org.springframework.jdbc.core.RowMapper;

import cn.com.fanyev5.baseservice.orm.entity.IEntity;

import java.util.List;
import java.util.Map;

/**
 * 通用EntityBinder
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-29
 */
public class BaseEntityBinder<T extends IEntity> {

    /**
     * 对应表名
     */
    private String table;

    /**
     * 对应字段ID
     */
    private EntityField entityFieldId;

    /**
     * 字段Map集, Key:{@link EntityField#attribueName}
     */
    private List<EntityField> entityFields;

    /**
     * SQL集合
     */
    private Map<String, String> entitySqls;

    private RowMapper<T> rowMapper;

    public final String getTable() {
        return table;
    }

    public final void setTable(String table) {
        this.table = table;
    }

    public EntityField getEntityFieldId() {
        return entityFieldId;
    }

    public void setEntityFieldId(EntityField entityFieldId) {
        this.entityFieldId = entityFieldId;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    public Map<String, String> getEntitySqls() {
        return entitySqls;
    }

    public void setEntitySqls(Map<String, String> entitySqls) {
        this.entitySqls = entitySqls;
    }

    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }
}
