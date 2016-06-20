/**
 * 
 */
package com.dianping.tiger.event;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.dianping.tiger.ScheduleServer;

/**
 * @author yuantengkai 事件执行器配置项<br/>
 *         <mail to: zjytk05@163.com/>
 */
public class EventConfig {

	/**
	 * 执行器名称
	 */
	private String handler;

	/**
	 * 执行器节点
	 */
	private List<Integer> nodeList;

	private int identifyCode;

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public List<Integer> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Integer> nodeList) {
		this.nodeList = nodeList;
	}

	public int getIdentifyCode() {
		return identifyCode;
	}

	public void setIdentifyCode(int identifyCode) {
		this.identifyCode = identifyCode;
	}
	
	/**
	 * 当前执行器的巡航设置,默认开启,
	 * 1. 全局设置ScheduleServer.enableNavigate为开启true，还需要判断当前执行器的情况
	 * 2. 全局设置ScheduleServer.enableNavigate为关闭false,则不判断当前执行器配置
	 */
	public boolean enableNavigate() {
		return ScheduleServer.getInstance().enableNavigate(handler);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
