package org.seckill.exception;

/**
 * @author Yuwl on 2016年6月15日
 */
public class SeckillException extends RuntimeException {

	/**
	 * @param message
	 * @param cause
	 */
	public SeckillException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public SeckillException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
