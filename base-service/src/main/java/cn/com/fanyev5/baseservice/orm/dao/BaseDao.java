package cn.com.fanyev5.baseservice.orm.dao;

import com.google.common.collect.ImmutableMap;

import cn.com.fanyev5.baseservice.orm.common.BaseEntityBinder;
import cn.com.fanyev5.baseservice.orm.common.EntityBinderUtil;
import cn.com.fanyev5.baseservice.orm.constants.SqlConstants;
import cn.com.fanyev5.baseservice.orm.entity.IEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

/**
 * Dao基础接口实现
 *
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public abstract class BaseDao<T extends IEntity> implements IDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    protected JdbcTemplate jdbcTemplate;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    /**
     * 获取EntityBinder
     *
     * @return
     */
    public abstract BaseEntityBinder<T> getEntityBinder();

    @Override
    public T save(T entity) {
        BaseEntityBinder entityBinder = getEntityBinder();
        String sql = (String) entityBinder.getEntitySqls().get(SqlConstants.SQL_KEY_INSERT);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String[] keyColumnNames = null;
        if (entityBinder.getEntityFieldId() != null) {
            keyColumnNames = new String[]{entityBinder.getEntityFieldId().getColumnName()};
        }
        int n = namedParameterJdbcTemplate.update(sql, EntityBinderUtil.newSqlParameterSource(entity), keyHolder, keyColumnNames);
        if (n != 0) {
            if (n > 1) {
                LOGGER.error("Exec sql result fail. sql: {}", sql);
            }
            entity.setId(keyHolder.getKey().longValue());
        }
        return entity;
    }

    @Override
    public List<T> save(List<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
        return entities;
    }

    @Override
    public boolean delete(T entity) {
        BaseEntityBinder entityBinder = getEntityBinder();
        String sql = (String) entityBinder.getEntitySqls().get(SqlConstants.SQL_KEY_DELETE);
        int n = namedParameterJdbcTemplate.update(sql, ImmutableMap.of(entityBinder.getEntityFieldId().getAttribueName(), entity.getId()));
        if (n > 1) {
            LOGGER.error("Exec sql result fail. sql: {}", sql);
        }
        return n != 0;
    }

    @Override
    public boolean update(T entity) {
        BaseEntityBinder entityBinder = getEntityBinder();
        String sql = (String) entityBinder.getEntitySqls().get(SqlConstants.SQL_KEY_UPDATE);
        int n = namedParameterJdbcTemplate.update(sql, EntityBinderUtil.newSqlParameterSource(entity));
        if (n > 1) {
            LOGGER.error("Exec sql result fail. sql: {}", sql);
        }
        return n != 0;
    }

    @Override
    public T get(long id, Class<T> clazz) {
        BaseEntityBinder<T> entityBinder = getEntityBinder();
        String sql = entityBinder.getEntitySqls().get(SqlConstants.SQL_KEY_GET);
        RowMapper<T> rowMapper = entityBinder.getRowMapper();
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, ImmutableMap.of(entityBinder.getEntityFieldId().getAttribueName(), id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
