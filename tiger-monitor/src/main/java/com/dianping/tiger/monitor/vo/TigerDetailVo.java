/**
 * 
 */
package com.dianping.tiger.monitor.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * @author yuantengkai
 * 
 */
public class TigerDetailVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1558448556327761446L;

	/**
	 * 任务id
	 */
	private Long taskId;
	
	/**
	 * 任务添加时间
	 */
	private Long addTime;
	
	/**
	 * 任务监控时间
	 */
	private Long monitorTime;
	
	/**
	 * handler组名
	 */
	private String handlerGroup;
	
	/**
	 * handler名字
	 */
	private String handler;
	
	/**
	 * 虚拟节点
	 */
	private Integer node;
	
	/**
	 * 重试次数
	 */
	private Integer retryTimes;
	
	/**
	 * 任务执行状态,see DispatchTaskService.TaskType
	 */
	private Integer status;
	
	/**
	 * 最早执行时间, 非空
	 */
	private Long earliestExecuteTime;
	
	/**
	 * 业务参数
	 */
	private String parameter;
	
	/**
	 * 真实的执行机器
	 */
	private String host;
	
	/**
	 * tigerTraceId
	 */
	private String ttid;
	
	/**
	 * 业务定义的唯一标示符,用于取消一个任务
	 */
	private String bizUniqueId;
	
	/**
	 * 任务备注
	 */
	private String remark;
	
	/**
	 * 执行结果
	 */
	private String executeResult;


	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getAddTime() {
		return addTime;
	}

	public void setAddTime(Long addTime) {
		this.addTime = addTime;
	}

	public Long getMonitorTime() {
		return monitorTime;
	}

	public void setMonitorTime(Long monitorTime) {
		this.monitorTime = monitorTime;
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

	public Integer getNode() {
		return node;
	}

	public void setNode(Integer node) {
		this.node = node;
	}

	public Integer getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getEarliestExecuteTime() {
		return earliestExecuteTime;
	}

	public void setEarliestExecuteTime(Long earliestExecuteTime) {
		this.earliestExecuteTime = earliestExecuteTime;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTtid() {
		return ttid;
	}

	public void setTtid(String ttid) {
		this.ttid = ttid;
	}

	public String getBizUniqueId() {
		return bizUniqueId;
	}

	public void setBizUniqueId(String bizUniqueId) {
		this.bizUniqueId = bizUniqueId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExecuteResult() {
		return executeResult;
	}

	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
	
	public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
