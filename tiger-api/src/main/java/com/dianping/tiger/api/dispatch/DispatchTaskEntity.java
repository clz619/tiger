/**
 * 
 */
package com.dianping.tiger.api.dispatch;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author yuantengkai
 * 任务操作对象
 */
public class DispatchTaskEntity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 9210838180567319401L;

	/**
	 * 任务id
	 */
	private Long id;
	
	/**
	 * 任务添加时间
	 */
	private Date addTime;
	
	/**
	 * 任务更新时间
	 */
	private Date updateTime;
	
	/**
	 * handler任务组，非空，取部署tiger应用的handlerGroup;handler里参数可以取自param.getHandlerGroup()
	 */
	private String handlerGroup;
	
	/**
	 * handler名字,非空
	 */
	private String handler;
	
	/**
	 * 虚拟节点:计算＝loadbalance%numOfvisualNode
	 */
	private Integer node;
	
	/**
	 * 负载均衡参数,非空
	 */
	private int loadbalance;
	
	/**
	 * 重试次数
	 */
	private Integer retryTimes;
	
	/**
	 * 状态,see DispatchTaskService.TaskType
	 */
	private Integer status;
	
	/**
	 * 最早执行时间, 非空
	 */
	private Date earliestExecuteTime;
	
	/**
	 * 业务参数,json格式,com.alibaba.fastjson.JSON.toJSONString()
	 */
	private String parameter;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public Date getEarliestExecuteTime() {
		return earliestExecuteTime;
	}

	public void setEarliestExecuteTime(Date earliestExecuteTime) {
		this.earliestExecuteTime = earliestExecuteTime;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public int getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(int loadbalance) {
		this.loadbalance = loadbalance;
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

	public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
