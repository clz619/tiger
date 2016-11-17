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
	
	/**
	 * 任务所在节点，分库分表时会用到此字段
	 */
	private Integer node;
	
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

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}
	

}
