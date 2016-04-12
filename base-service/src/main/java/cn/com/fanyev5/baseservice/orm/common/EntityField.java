package cn.com.fanyev5.baseservice.orm.common;

/**
 * Entity field
 *
 * @author fanqi427@gmail.com
 * @since 2013-08-06
 */
public class EntityField {

    /**
     * 属性名
     */
    private final String attribueName;

    /**
     * 字段名
     */
    private final String columnName;

    /**
     * 是否为ID
     */
    private final boolean idField;

    /**
     * 是否为自增ID
     */
    private final boolean autoId;

    /**
     * ID Sequence
     */
    private final String autoIdSequence;

    /**
     * 类型
     */
    private final Class<?> type;

    public EntityField(String attribueName, String columnName, boolean idField, boolean autoId, String autoIdSequence, Class<?> type) {
        this.attribueName = attribueName;
        this.columnName = columnName;
        this.idField = idField;
        this.autoId = autoId;
        this.autoIdSequence = autoIdSequence;
        this.type = type;
    }

    public String getAttribueName() {
        return attribueName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isIdField() {
        return idField;
    }

    public boolean isAutoId() {
        return autoId;
    }

    public String getAutoIdSequence() {
        return autoIdSequence;
    }

    public Class<?> getType() {
        return type;
    }

}
