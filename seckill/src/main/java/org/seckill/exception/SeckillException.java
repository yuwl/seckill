package org.seckill.exception;

/**
 * 自定义运行时异常
 * @author Yuwl on 2016年6月15日
 */
public class SeckillException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8181529577964661145L;

	/**
	 * @param message
	 * @param cause
	 */
	public SeckillException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SeckillException(String message) {
		super(message);
	}

}
