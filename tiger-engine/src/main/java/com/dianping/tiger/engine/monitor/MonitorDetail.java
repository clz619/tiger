/**
 * 
 */
package com.dianping.tiger.engine.monitor;

/**
 * @author yuantengkai
 *
 */
public class MonitorDetail {

private String handlerGroup;
	
	private String handler;

	private int success;// 1-成功

	private int costTime;// ms
	
	public MonitorDetail(String handlerGroup, String handler,int success,int costTime){
		this.handlerGroup = handlerGroup;
		this.handler=handler;
		this.success=success;
		this.costTime = costTime;
	}

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getCostTime() {
		return costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

}
