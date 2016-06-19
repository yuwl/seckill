package org.seckill.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yuwl on 2016年6月13日
 */
//配置Spring和Junit配合，Junit启动时加载Spring容器
//spring-test,junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessSkilled() throws Exception{
		/**
		 * 使用insert ignore into success_killed(seckill_id,user_phone)
		 * 联合产键重复插入不会报错
		 */
		int count = successKilledDao.insertSuccessKill(1000, 13512345678L);
		System.out.println("count-insertSuccessSkilled :"+count);
	}
	
	@Test
	public void testQueryByIdWithSeckill() throws Exception{
		SuccessKilled sk = successKilledDao.queryByIdWithSeckill(1000, 13512345678L);
		System.out.println(sk);
		System.out.println(sk.getSeckill());
	}
	
}
