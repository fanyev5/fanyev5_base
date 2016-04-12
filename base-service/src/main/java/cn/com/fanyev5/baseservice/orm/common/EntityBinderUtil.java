package cn.com.fanyev5.baseservice.orm.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.com.fanyev5.baseservice.orm.constants.SqlConstants;
import cn.com.fanyev5.baseservice.orm.entity.IEntity;
import cn.com.fanyev5.baseservice.orm.exception.ORMException;
import cn.com.fanyev5.baseservice.orm.util.SqlUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * EntityBinder管理工具
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public final class EntityBinderUtil {

    private EntityBinderUtil() {
    }

    /**
     * 构建<code>SqlParameterSource</code>
     *
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends IEntity> SqlParameterSource newSqlParameterSource(T entity) {
        return new BeanPropertySqlParameterSource(entity);
    }

    /**
     * 构建BaseEntityBinder
     *
     * @param entityClass
     * @return
     */
    public static <T extends IEntity> BaseEntityBinder<T> buildEntityBinder(Class<T> entityClass) {
        try {
            // 获取entity信息
            String tableName = null;
            EntityField entityFieldId = null;

            // 解析Class注解
            {
                Entity annEntity = entityClass.getAnnotation(Entity.class);
                if (annEntity == null) {
                    throw new IllegalArgumentException("The Entity class must have an javax.persistence.Entity annotation.");
                }
                Table annTable = entityClass.getAnnotation(Table.class);
                if (annTable != null) {
                    tableName = annTable.name();
                }
                Preconditions.checkArgument(!Strings.isNullOrEmpty(tableName), "Can't find the @Table(name=) annotation.");
            }

            // 解析属性
            Set<EntityField> entityFields = Sets.newHashSet();
            {
                getFields(entityFields, entityClass);
                for (EntityField entityField : entityFields) {
                    if (entityField.isIdField()) {
                        Preconditions.checkState(entityFieldId == null, "Entity @Id number > 1.");
                        entityFieldId = entityField;
                    }
                }
                Preconditions.checkNotNull(entityFieldId, "Can't find the id field for " + entityClass);
            }

            // 构建Binder
            BaseEntityBinder entityBinder = BaseEntityBinder.class.newInstance();
            entityBinder.setTable(tableName);
            entityBinder.setEntityFieldId(entityFieldId);

            entityBinder.setEntityFields(ImmutableList.<EntityField>builder().addAll(entityFields).build());

            // 构建通用SQL
            {
                Map<String, String> entitySqlMap = Maps.newHashMap();
                entitySqlMap.put(SqlConstants.SQL_KEY_INSERT, SqlUtil.genInsert(entityBinder.getTable(), entityBinder.getEntityFields()));
                entitySqlMap.put(SqlConstants.SQL_KEY_DELETE, SqlUtil.genDelete(entityBinder.getTable(), entityBinder.getEntityFieldId()));
                entitySqlMap.put(SqlConstants.SQL_KEY_UPDATE, SqlUtil.genUpdate(entityBinder.getTable(), entityBinder.getEntityFields()));
                entitySqlMap.put(SqlConstants.SQL_KEY_GET, SqlUtil.genGetById(entityBinder.getTable(), entityBinder.getEntityFields()));
                entityBinder.setEntitySqls(ImmutableMap.<String, String>builder().putAll(entitySqlMap).build());
            }

            // 构建RowMapper
            entityBinder.setRowMapper(new CustomRowMapper(entityClass, entityBinder.getEntityFields()));

            return entityBinder;
        } catch (Exception e) {
            throw new ORMException(String.format("build EntityBinder fail. %s", entityClass), e);
        }
    }

    /**
     * 获取Entity field集合, 递归处理
     *
     * @param entityFields
     * @param clazz
     * @return
     */
    private static Set<EntityField> getFields(Set<EntityField> entityFields, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String attribueName = field.getName();
                String columnName = Strings.isNullOrEmpty(columnAnnotation.name()) ? field.getName() : columnAnnotation.name();
                boolean idField = (field.getAnnotation(Id.class) != null);
                boolean autoId = false;
                String autoIdSequence = null;
                if (idField) {
                    GeneratedValue idGenerate = field.getAnnotation(GeneratedValue.class);
                    if (idGenerate != null) {
                        switch (idGenerate.strategy()) {
                            case AUTO:
                                autoId = true;
                                break;
                            case SEQUENCE:
                                autoId = true;
                                autoIdSequence = idGenerate.generator();
                                break;
                            default:
                                throw new ORMException("Id generate is not found.");
                        }
                    }
                }
                EntityField entityField = new EntityField(attribueName, columnName, idField, autoId, autoIdSequence, field.getType());
                entityFields.add(entityField);
            }
        }
        if (IEntity.class.isAssignableFrom(clazz.getSuperclass())) {
            getFields(entityFields, clazz.getSuperclass());
        }
        return entityFields;
    }

    /**
     * 自定义RowMapper
     *
     * @param <T>
     */
    private static final class CustomRowMapper<T extends IEntity> implements RowMapper<T> {
        private Class<T> entityClass;
        private List<EntityField> entityFields;

        private CustomRowMapper(Class<T> entityClass, List<EntityField> entityFields) {
            this.entityClass = entityClass;
            this.entityFields = entityFields;
        }

        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {
            EntityField entityField = null;
            try {
                T entity = entityClass.newInstance();
                Iterator<EntityField> iterator = entityFields.iterator();
                while (iterator.hasNext()) {
                    entityField = iterator.next();
                    if (entityField.getType() == Date.class) {
                        Timestamp time = rs.getTimestamp(entityField.getColumnName());
                        if (time != null) {
                            BeanUtils.setProperty(entity, entityField.getAttribueName(), new Date(time.getTime()));
                        }
                    } else {
                        BeanUtils.setProperty(entity, entityField.getAttribueName(), rs.getObject(entityField.getColumnName()));
                    }
                }
                return entity;
            } catch (Exception e) {
                String msg = "";
                if (entityField == null) {
                    msg = "Entity field is NULL.";
                } else {
                    msg = String.format("Entity field set fail. attribue: %s, column: %s, type: %s", entityField.getAttribueName(), entityField.getColumnName(), entityField.getType());
                }
                throw new ORMException(msg, e);
            }
        }
    }

}
