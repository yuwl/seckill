package org.seckill.exception;

/**
 * 秒杀关闭异常
 * @author Yuwl on 2016年6月15日
 */
public class SeckillCloseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6290425115435104747L;

	/**
	 * @param message
	 * @param cause
	 */
	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SeckillCloseException(String message) {
		super(message);
	}

	
	
}
