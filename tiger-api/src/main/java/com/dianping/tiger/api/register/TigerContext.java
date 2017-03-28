package com.dianping.tiger.api.register;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * tiger注册上下文信息
 * @author yuantengkai
 *
 */
public class TigerContext implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4641853519222836375L;

	private String handlerGroup;
	
	private String registerVersion;
	
	//时间搓，单位s
	private long registerTime;
	
	private String hostName;

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getRegisterVersion() {
		return registerVersion;
	}

	public void setRegisterVersion(String registerVersion) {
		this.registerVersion = registerVersion;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
