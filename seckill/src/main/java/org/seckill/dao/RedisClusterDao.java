package org.seckill.dao;

import redis.clients.jedis.JedisCluster;

/**
 * Redis集群测试
 * @author Yuwl on 2016年11月13日
 */
public class RedisClusterDao {
	
	JedisCluster jedisCluster;

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	public String setCache(String key, String value){
		return jedisCluster.set(key, value);
	}
	
	public String getCache(String key){
		return jedisCluster.get(key);
	}

}
