package org.seckill.service;

import java.util.List;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeadException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * 站在使用者的角度设计接口
 * 三方面：方法定义粒度、参数、返回类型
 * @author Yuwl on 2016年6月15日
 */
public interface SeckillService {
	
	/**
	 * 查询所有秒杀记录
	 * @return
	 */
	List<Seckill> queryAll();

	/**
	 * 根据id查询秒杀对象
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * 秒杀开启时输出接口地址
	 * 否则输出系统时间和秒杀时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) 
			throws SeckillException,RepeadException,SeckillCloseException;
	
}
