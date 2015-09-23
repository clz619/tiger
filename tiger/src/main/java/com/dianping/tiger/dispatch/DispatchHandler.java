/**
 * 
 */
package com.dianping.tiger.dispatch;

/**
 * @author yuantengkai <mail to: zjytk05@163.com/>
 */
public interface DispatchHandler {

	/**
	 * 任务分发执行入口
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public DispatchResult invoke(DispatchParam param) throws Exception;

}
