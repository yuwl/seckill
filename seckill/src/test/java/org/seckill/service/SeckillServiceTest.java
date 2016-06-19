package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yuwl on 2016年6月15日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:spring/spring-dao.xml",
		"classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	
	private Logger looger = LoggerFactory.getLogger(SeckillServiceTest.class);
	
	@Autowired
	private SeckillService seckillService;
	
	@Test
	public void testQueryAll(){
		List<Seckill> list = seckillService.queryAll();
		looger.info("list={}",list);
	}
	
	@Test
	public void testQueryById(){
		Seckill seckill = seckillService.queryById(1000);
		looger.info("seckill={}",seckill);
	}
	
	@Test
	public void testExecuteSeckill(){
		Exposer exposer = seckillService.exportSeckillUrl(1000);
		if(exposer.isExposed()){
			SeckillExecution se = seckillService.executeSeckill(1000, 13523455689L, exposer.getMD5());
			looger.info("seckill execute={}",se);
		}
	}
	
}
