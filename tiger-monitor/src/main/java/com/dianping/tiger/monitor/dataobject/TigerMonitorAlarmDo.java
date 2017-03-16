/**
 * 
 */
package com.dianping.tiger.monitor.dataobject;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author yuantengkai
 * tiger报警配置对象
 */
public class TigerMonitorAlarmDo {
	
	private Long id;
	
	private Date addTime;
	
	private Date updateTime;
	
	private String handlerGroup;
	
	private String handlerName;
	
	/**
	 * 报警最少连续失败次数
	 */
	private int leastFailNum;
	
	/**
	 * 报警失败间隔次数
	 */
	private int intervalFailNum;
	
	/**
	 * 邮件联系人
	 */
	private String mailReceives;
	
	/**
	 * 是否关闭报警；0-不关闭；1-关闭
	 */
	private int offFlag;

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

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public int getLeastFailNum() {
		return leastFailNum;
	}

	public void setLeastFailNum(int leastFailNum) {
		this.leastFailNum = leastFailNum;
	}

	public int getIntervalFailNum() {
		return intervalFailNum;
	}

	public void setIntervalFailNum(int intervalFailNum) {
		this.intervalFailNum = intervalFailNum;
	}

	public String getMailReceives() {
		return mailReceives;
	}

	public void setMailReceives(String mailReceives) {
		this.mailReceives = mailReceives;
	}
	
	public int getOffFlag() {
		return offFlag;
	}

	public void setOffFlag(int offFlag) {
		this.offFlag = offFlag;
	}
	
	/**
	 * 报警是否开启
	 * @return
	 */
	public boolean isOpen(){
		return this.offFlag == 0 && this.leastFailNum > 0;
	}
	
	/**
	 * 是否触发报警
	 * @param failNum
	 * @return
	 */
	public boolean isAlarmTrigger(int failNum){
		if(failNum < this.leastFailNum){
			return false;
		}
		if((failNum - this.leastFailNum) % this.intervalFailNum == 0){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
