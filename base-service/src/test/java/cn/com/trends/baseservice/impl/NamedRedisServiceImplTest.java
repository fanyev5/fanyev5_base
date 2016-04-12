package cn.com.trends.baseservice.impl;

import com.google.common.base.Function;

import cn.com.fanyev5.baseservice.redis.client.INamedRedisService;
import cn.com.fanyev5.baseservice.redis.client.IRedis;
import cn.com.fanyev5.baseservice.redis.client.impl.NamedRedisServiceImpl;
import cn.com.fanyev5.baseservice.redis.message.MessageSub;

import org.junit.*;

/**
 * RedisService ImplTest
 * 
 * @author fanqi427@gmail.com
 * @since 2013-07-26
 */
public class NamedRedisServiceImplTest {

	private String GROUP_ENTITY = "group_redis_entity";
	private String GROUP_API = "group_redis_api";
	private String msgKey = "t_msg";

	private INamedRedisService redisService;

	@Before
	public void before() {
		// 初始化
		redisService = new NamedRedisServiceImpl();
	}

	@After
	public void after() {
		// 关闭
		redisService.destroy();
	}

	@Test
	/**
	 * 因为先要订阅再push才能收到信息，如果push在前，订阅在后，就收不到消息
	 * @throws InterruptedException
	 */
	public void test() throws InterruptedException {
		// testSubscribe();
		// testPublish();
		
		// 测试通过redis publish一个通知
		//set();
		del();
	}

	@Test
	// @Ignore
	public void testSubscribe() throws InterruptedException {
		IRedis redis = redisService.get(GROUP_API);
		Assert.assertNotNull(redis);

		Function<String, Void> fun = new Function<String, Void>() {
			public Void apply(String msg) {
				System.out.println("receive msg: " + msg);
				return null;
			}
		};
		final MessageSub messageSub = new MessageSub(fun);
		System.out.println("subscribe");
		redis.subscribe(msgKey, messageSub);
		System.out.println("subscribe end ");
	}

	@Test
	// @Ignore
	public void testPublish() throws InterruptedException {
		System.out.println("testPublish");
		IRedis redis = redisService.get(GROUP_API);
		Assert.assertNotNull(redis);
		for (int i = 0; i < 10; i++) {
			String msg = "msg_" + i;
			redis.publish(msgKey, msg);
			System.out.println("publish msg: " + msg);
			Thread.sleep(100L);
		}
	}
	
	@Test
	// @Ignore
	public void set() throws InterruptedException {
		System.out.println("set");
		IRedis redis = redisService.get(GROUP_API);
		Assert.assertNotNull(redis);
		
		Integer i=(Integer)redis.getObject("1_d_ads");
		redis.setObject("1_d_ads",i+1, 0);
		System.out.println(redis.getObject("1_d_ads"));
		
		AdChangeRule changeRule = new AdChangeRule(AdChangeRuleEnum.RELOAD,
				null);
		redis.publish("1_d_ads_ler",
				RuleUtil.genAdChangeRule(changeRule));

	}
	
	@Test
	// @Ignore
	public void del() throws InterruptedException {
		System.out.println("del");
		IRedis redis = redisService.get(GROUP_API);
		Assert.assertNotNull(redis);
		redis.del("1_d_ads");
	}
}
