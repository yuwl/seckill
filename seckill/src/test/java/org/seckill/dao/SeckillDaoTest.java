package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yuwl on 2016年6月12日
 */
//配置Spring和Junit配合，Junit启动时加载Spring容器
//spring-test,junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById() throws Exception{
		Seckill seckill = seckillDao.queryById(1000);
		System.out.println(seckill);
	}
	
	
	@Test
	public void testQueryAll() throws Exception{
		/**
		 * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offset' not found. Available parameters are [1, 0, param1, param2]
		 * @throws Exception
		 */
		/*	
		 * List<Seckill> queryAll(int offset,int limit);
		   Java没有保存形参的记录：queryAll(int offset,int limit) -> queryAll(arg0,arg1)
		        解决办法：List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);
		*/
		List<Seckill> list = seckillDao.queryAll(0, 100);
		for(Seckill seckill : list){
			System.out.println(seckill);
		}
	}
	
	@Test
	public void testReduceNumber() throws Exception{
		int count = seckillDao.reduceNumber(1000, new Date());
		System.out.println("count: "+count);
	}

}
