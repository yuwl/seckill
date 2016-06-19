package org.seckill.dto;

/**
 * 暴露秒杀地址DTO
 * @author Yuwl on 2016年6月15日
 */
public class Exposer {

	private boolean exposed;
	
	private String MD5;
	
	private long seckillId;
	
	private long now;
	
	private long start;
	
	private long end;

	/**
	 * @param exposed
	 * @param mD5
	 * @param seckillId
	 */
	public Exposer(boolean exposed, String mD5, long seckillId) {
		super();
		this.exposed = exposed;
		MD5 = mD5;
		this.seckillId = seckillId;
	}

	/**
	 * @param exposed
	 * @param now
	 * @param start
	 * @param end
	 */
	public Exposer(boolean exposed, long now, long start, long end) {
		super();
		this.exposed = exposed;
		this.now = now;
		this.start = start;
		this.end = end;
	}

	/**
	 * @param exposed
	 * @param seckillId
	 */
	public Exposer(boolean exposed, long seckillId) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	/**
	 * @return the exposed
	 */
	public boolean isExposed() {
		return exposed;
	}

	/**
	 * @param exposed the exposed to set
	 */
	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	/**
	 * @return the mD5
	 */
	public String getMD5() {
		return MD5;
	}

	/**
	 * @param mD5 the mD5 to set
	 */
	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	/**
	 * @return the seckillId
	 */
	public long getSeckillId() {
		return seckillId;
	}

	/**
	 * @param seckillId the seckillId to set
	 */
	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	/**
	 * @return the now
	 */
	public long getNow() {
		return now;
	}

	/**
	 * @param now the now to set
	 */
	public void setNow(long now) {
		this.now = now;
	}

	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	
}
