package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.RedisDao;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeadException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author Yuwl on 2016年6月15日
 */
//@Component：表示所有注解 @Dao @Service @Controller
@Service
public class SeckillServiceImpl implements SeckillService {
	
	Logger logger = LoggerFactory.getLogger(SeckillServiceImpl.class);
	
	private final String slat = "fdsakofjqewjopif2149u31495324i-654#@$#@%@$%#$233";
	
	@Autowired
	private SeckillDao seckillDao;
	
	@Autowired
	private RedisDao redisDao;
	
	@Autowired
	private SuccessKilledDao successKilledDao;

	/* (non-Javadoc)
	 * @see org.seckill.service.SeckillService#queryAll()
	 */
	@Override
	public List<Seckill> queryAll() {
		return seckillDao.queryAll(0, 4);
	}

	/* (non-Javadoc)
	 * @see org.seckill.service.SeckillService#queryById(long)
	 */
	@Override
	public Seckill queryById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	/* (non-Javadoc)
	 * @see org.seckill.service.SeckillService#exportSeckillUrl(long)
	 */
	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		//优化点：缓存优化，超时基础上维护一致性
		//1.redis获取
		Seckill seckill = redisDao.getSeckill(seckillId);
		if(seckill == null){
			//2.从数据库取
			seckill = seckillDao.queryById(seckillId);
			if(seckill == null){
				return new Exposer(false, seckillId);
			}else{
				//3.放入redis
				redisDao.putSeckill(seckill);
			}
		}
		
		Date start = seckill.getStartTime();
		Date end = seckill.getEndTime();
		Date now = new Date();
		if(now.getTime() < start.getTime()
				|| now.getTime() > end.getTime()){
			return new Exposer(false, now.getTime(),start.getTime(), end.getTime());
		}
		String md5 = getMd5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	
	private String getMd5(long seckillId){
		String base = seckillId + "/" + slat;
		String mds = DigestUtils.md5DigestAsHex(base.getBytes());//Spring Md5加密工具
		return mds;
	}

	/* (non-Javadoc)
	 * @see org.seckill.service.SeckillService#executeSeckill(long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	/**
	 * 1.开发团队达到一致约定，明确标注事务方法的编程风格
	 * 2.保证事务方法的执行时间尽可能短，不要穿插其它网络操作，如RPC/Http/缓存(运行时间长)，或者剥离到事务方法外部，做一个更上层的方法
	 * 3.不是所有方法都需要事务
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, RepeadException,
			SeckillCloseException {
		if(md5 == null || !md5.equals(getMd5(seckillId))){
			throw new SeckillException("seckill data rewrite");
		}
		try {
			//执行秒杀逻辑：减库存+保存秒杀记录
			//优化mysql行级锁的网络延迟及GC等待时间
			//调整秒杀逻辑：保存秒杀记录+减库存
			//先执行保存秒杀记录，因为这一步不涉及行级锁的等待，可以节省时间
			//降低Mysql rowLock的持有时间，因为只有减库存(update)才会触发行级锁(同一个商品在秒杀时大量购买，同时update)		
			//记录购买记录
			
			int insertCount = successKilledDao.insertSuccessKill(seckillId, userPhone);
			if(insertCount <= 0){
				//重复秒杀
				throw new RepeadException("seckill repeat");
			}else{
				int updateCount = seckillDao.reduceNumber(seckillId, new Date());
				if(updateCount <= 0){
					//没有更新到记录
					throw new SeckillCloseException("seckill is closed");
				}else{
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		}catch(SeckillCloseException e1){
			throw e1;
		}catch(RepeadException e2){
			throw e2;
		}catch (Exception e) {
			logger.error(e.getMessage());
			//将所有编译期异常转化为运行期异常，只有抛出运行期异常，Spring事务才会回滚
			//默认spring事务只在发生未被捕获的runtimeexcetpion时才回滚
			throw new SeckillException("SeckillException inner error: "+e.getMessage());
		}
	}

}
