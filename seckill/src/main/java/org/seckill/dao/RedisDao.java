package org.seckill.dao;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Yuwl on 2016年6月23日
 */
public class RedisDao {
	
	private Logger logger = LoggerFactory.getLogger(RedisDao.class);
	
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	private JedisPool jedisPool;
	
	public RedisDao(String ip, int port){
		this.jedisPool = new JedisPool(ip, port);
	}
	
	public Seckill getSeckill(long seckillId){
		try {
			Jedis jedis = jedisPool.getResource();
			String key = "seckill:"+seckillId;
			try {
				//jedis并没有实现内部序列化操作
				//get->bytes[]->反系列化->object[seckill]
				//采用自定义系列化：protostuff
				byte[] bytes = jedis.get(key.getBytes());//从缓存里取
				if(bytes != null){
					Seckill seckill = schema.newMessage();//空对象
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);//省时间+省空间
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public String putSeckill(Seckill seckill){
		//set object[seckill] -> 序例化 -> byte[]
		try {
			Jedis jedis = jedisPool.getResource();
			String key = "seckill:"+seckill.getSeckillId();
			try {
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				//超时缓存
				int timeout = 60*60;//1小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);//正确返回ok,错误返回错误信息
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
