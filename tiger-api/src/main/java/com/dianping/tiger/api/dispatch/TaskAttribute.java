/**
 * 
 */
package com.dianping.tiger.api.dispatch;


/**
 * @author yuantengkai
 * 任务执行属性
 */
public class TaskAttribute {
	
	/**
	 * 执行机器, handler里参数可以取自ScheduleServer.getInstance().getServerName()
	 */
	private String hostName;
	
	/**
	 * tigerTraceId,handler里参数可以取自param.getTraceId()
	 */
	private String ttid;
	
	public TaskAttribute(String hostName, String ttid){
		this.hostName = hostName;
		this.ttid = ttid;
	}

	public String getHostName() {
		return hostName;
	}

	public String getTtid() {
		return ttid;
	}
	

}
