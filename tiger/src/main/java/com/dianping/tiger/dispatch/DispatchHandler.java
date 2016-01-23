/**
 * 
 */
package com.dianping.tiger.dispatch;

/**
 * @author yuantengkai <mail to: zjytk05@163.com/><br/>
 * 1. java类实现，需要配置spring bean<br/>
 * 2. groovy代码实现，通过GroovyBeanType来制定handler的多例、单例
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
