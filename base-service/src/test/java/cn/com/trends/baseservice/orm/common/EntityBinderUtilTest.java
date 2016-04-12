package cn.com.trends.baseservice.orm.common;

import cn.com.fanyev5.baseservice.orm.common.BaseEntityBinder;
import cn.com.fanyev5.baseservice.orm.common.EntityBinderUtil;
import cn.com.trends.baseservice.orm.entity.Test2Entity;
import cn.com.trends.baseservice.orm.entity.Test3Entity;
import cn.com.trends.baseservice.orm.entity.TestEntity;
import org.junit.Assert;
import org.junit.Test;

/**
 * EntityBinderUtil Test
 * 
 * @author fanqi427@gmail.com
 * @since 2013-08-06
 */
public class EntityBinderUtilTest {

	@Test
	public void test() {
		BaseEntityBinder<TestEntity> entityBinder = EntityBinderUtil
				.buildEntityBinder(TestEntity.class);
		Assert.assertNotNull(entityBinder);
		System.out.println(entityBinder.getTable());
		System.out.println(entityBinder.getEntityFieldId());
	}

	@Test
	public void test2() {
		BaseEntityBinder<Test2Entity> entityBinder = EntityBinderUtil
				.buildEntityBinder(Test2Entity.class);
		Assert.assertNotNull(entityBinder);
		System.out.println(entityBinder.getTable());
		System.out.println(entityBinder.getEntityFieldId());
	}

	@Test
	public void test3() {
		BaseEntityBinder<Test3Entity> entityBinder = EntityBinderUtil
				.buildEntityBinder(Test3Entity.class);
		Assert.assertNotNull(entityBinder);
		System.out.println(entityBinder.getTable());
		System.out.println(entityBinder.getEntityFieldId());

	}
}
