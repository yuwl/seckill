package org.seckill.enums;

/**
 * 枚举实现数据字典常量
 * @author Yuwl on 2016年6月15日
 */
public enum SeckillStateEnum {

	SUCCESS(1,"秒杀成功"),
	END(0,"秒杀结束"),
	REPEAT_KILL(-1,"重复秒杀"),
	INSERT_ERROR(-2,"系统异常"),
	DATA_REWRITE(-3,"数据篡改");
	
	private int state;
	
	private String stateInfo;

	/**
	 * @param state
	 * @param stateInfo
	 */
	private SeckillStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @return the stateInfo
	 */
	public String getStateInfo() {
		return stateInfo;
	}
	
	public static SeckillStateEnum stateOf(int index){
		for(SeckillStateEnum state : values()){
			if(state.getState() == index){
				return state;
			}
		}
		return null;
	}
}
