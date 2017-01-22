package com.dianping.tiger.monitor.dataobject;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 监控结果记录
 * @author xuxueli 2015-8-31 18:24:51
 */
public class TigerMonitorRecordDo {

	private Long id;
	
	private Date addTime;
	
	private Date updateTime;
	
	private Date monitorTime;	// 监控时间
	private String handlerGroup;	// handlerGroup名称
	private String handlerName;	// handle名称
	private String hostName;	// 服务器host名称
	private int totalNum;	// 监控total数
	private int sucNum;		// 监控success数
	private int failNum;	// 监控fail数
	private long avgCost;		// avg耗时
	private long maxCost;		// MAX耗时
	private long minCost;		// MIN耗时
	
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

	public Date getMonitorTime() {
		return monitorTime;
	}

	public void setMonitorTime(Date monitorTime) {
		this.monitorTime = monitorTime;
	}

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getSucNum() {
		return sucNum;
	}

	public void setSucNum(int sucNum) {
		this.sucNum = sucNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public long getAvgCost() {
		return avgCost;
	}

	public void setAvgCost(long avgCost) {
		this.avgCost = avgCost;
	}

	public long getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(long maxCost) {
		this.maxCost = maxCost;
	}

	public long getMinCost() {
		return minCost;
	}

	public void setMinCost(long minCost) {
		this.minCost = minCost;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
