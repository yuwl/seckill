package org.seckill.dao;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Yuwl on 2016年11月13日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-redisCluster.xml"})
public class RedisClusterTest {
	
	@Autowired
	private RedisClusterDao redisClusterDao;
	
	@Test
	public void testRedisClusterByAPi(){
		Set<HostAndPort> jedisClusterNodes = new HashSet<>();
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7001));
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7002));
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7003));
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7004));
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7005));
		jedisClusterNodes.add(new HostAndPort("192.168.60.128", 7006));
		
		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxTotal(100);
		cfg.setMaxIdle(20);
		cfg.setMaxWaitMillis(-1);
		cfg.setTestOnBorrow(true);
		JedisCluster jc = new JedisCluster(jedisClusterNodes, 6000,1000,cfg);
		
		System.out.println(jc.set("age", "20"));
		System.out.println(jc.set("sex", "男"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("name"));
		System.out.println(jc.get("age"));
		System.out.println(jc.get("sex"));
		jc.close();
	}
	
	@Test
	public void testRedisClusterByConfig(){
		System.out.println(redisClusterDao.getCache("name"));
		System.out.println(redisClusterDao.setCache("adr", "bj"));
		System.out.println(redisClusterDao.getCache("adr"));
	}

}
