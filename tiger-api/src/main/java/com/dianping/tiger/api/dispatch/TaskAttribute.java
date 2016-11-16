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
	
	private long taskId;
	
	private String bizUniqueId;
	
	public TaskAttribute(String hostName, String ttid){
		this.hostName = hostName;
		this.ttid = ttid;
	}
	
	public TaskAttribute(){
		
	}

	public String getHostName() {
		return hostName;
	}

	public String getTtid() {
		return ttid;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getBizUniqueId() {
		return bizUniqueId;
	}

	public void setBizUniqueId(String bizUniqueId) {
		this.bizUniqueId = bizUniqueId;
	}
	

}
