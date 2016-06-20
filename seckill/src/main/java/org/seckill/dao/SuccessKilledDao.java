package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * 秒杀成功Dao接口
 * @author Yuwl on 2016年6月12日
 */
public interface SuccessKilledDao {
	
	/**
	 * 插入购买明细，可过滤重复
	 * @param seckillId
	 * @param userPhone
	 * @return
	 */
	int insertSuccessKill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);
	
	/**
	 * 查询秒杀明细并携带秒杀对象
	 * @param seckillId
	 * @return
	 */
	SuccessKilled queryByIdWithSeckill(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

}
