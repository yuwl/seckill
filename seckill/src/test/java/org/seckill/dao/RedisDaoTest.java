package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yuwl on 2016年6月23日
 */
//手动注册redisDao
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
	
	private long seckillId=1001;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testSeckill(){
		//get and put
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill == null){
			seckill = seckillDao.queryById(seckillId);
			if(seckill != null){
				String res = redisDao.putSeckill(seckill);
				System.out.println("result: "+res);
			}
		}
		System.out.println("seckill: "+seckill);
	}
}
