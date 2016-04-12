package cn.com.fanyev5.baseservice.orm.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import cn.com.fanyev5.baseservice.orm.common.EntityField;
import cn.com.fanyev5.baseservice.orm.entity.IEntity;
import joptsimple.internal.Strings;

import java.util.List;

/**
 * SQL生成工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-29
 */
public final class SqlUtil {

    private SqlUtil() {
    }

    /**
     * 生成Insert语句
     *
     * @param tableName    表名
     * @param entityFields EntityField集合
     * @return
     */
    public static <T extends IEntity> String genInsert(String tableName, List<EntityField> entityFields) {
        Preconditions.checkNotNull(entityFields);
        List<String> columnFieldNames = Lists.newArrayList();
        List<String> columnValueNames = Lists.newArrayList();
        for (EntityField entityField : entityFields) {
            if (entityField.isAutoId()) {
                // 拼接AutoId Sequence
                if (!Strings.isNullOrEmpty(entityField.getAutoIdSequence())) {
                    columnFieldNames.add(entityField.getColumnName());
                    columnValueNames.add(entityField.getAutoIdSequence() + ".nextval");
                }
            } else {
                columnFieldNames.add(entityField.getColumnName());
                columnValueNames.add(':' + entityField.getAttribueName());
            }
        }
        return String.format("INSERT INTO %s(%s) VALUES(%s)", tableName, Strings.join(columnFieldNames, ","), Strings.join(columnValueNames, ","));
    }

    /**
     * 生成Delete语句
     *
     * @param tableName     表名
     * @param entityFieldId ID EntityField
     * @return
     */
    public static String genDelete(String tableName, EntityField entityFieldId) {
        Preconditions.checkNotNull(entityFieldId);
        return String.format("DELETE FROM %s WHERE %s=:%s", tableName, entityFieldId.getColumnName(), entityFieldId.getAttribueName());
    }

    /**
     * 生成Update语句
     *
     * @param tableName    表名
     * @param entityFields EntityField集合
     * @return
     */
    public static <T extends IEntity> String genUpdate(String tableName, List<EntityField> entityFields) {
        Preconditions.checkNotNull(entityFields);
        String columnIdNames = "";
        List<String> columnNames = Lists.newArrayList();
        for (EntityField entityField : entityFields) {
            if (entityField.isAutoId()) {
                columnIdNames = entityField.getColumnName() + "=:" + entityField.getAttribueName();
                continue;
            }
            columnNames.add(entityField.getColumnName() + "=:" + entityField.getAttribueName());
        }
        return String.format("UPDATE %s SET %s WHERE %s", tableName, Strings.join(columnNames, ","), columnIdNames);
    }

    /**
     * 生成Select语句
     *
     * @param tableName    表名
     * @param entityFields EntityField集合
     * @return
     */
    public static String genGetById(String tableName, List<EntityField> entityFields) {
        Preconditions.checkNotNull(entityFields);
        String columnIdNames = "";
        List<String> columnNames = Lists.newArrayList();
        for (EntityField entityField : entityFields) {
            if (entityField.isAutoId()) {
                columnIdNames = entityField.getColumnName() + "=:" + entityField.getAttribueName();
            }
            columnNames.add(entityField.getColumnName());
        }
        return String.format("SELECT %s FROM %s WHERE %s", Strings.join(columnNames, ","), tableName, columnIdNames);
    }
}
