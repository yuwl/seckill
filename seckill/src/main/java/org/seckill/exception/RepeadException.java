package org.seckill.exception;

/**
 * 重复秒杀异常-运行期异常
 * @author Yuwl on 2016年6月15日
 */
public class RepeadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3030123493790962883L;

	/**
	 * @param message
	 * @param cause
	 */
	public RepeadException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RepeadException(String message) {
		super(message);
	}

	
}
