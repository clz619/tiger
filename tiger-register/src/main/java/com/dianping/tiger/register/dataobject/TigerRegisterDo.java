package com.dianping.tiger.register.dataobject;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * 集群注册实体类
 * @author yuantengkai
 *
 */
public class TigerRegisterDo{
	

	private Long id;
	
	private Date addTime;
	
	private Date updateTime;
	
	//与hostName做一个唯一性约束
	private String handlerGroup;
	
	private String registerVersion;
	
	//时间搓
	private long registerTime;
	
	//负责的虚拟节点
	private String nodes;
	
	private String hostName;
	
	//乐观锁
	private long version;

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

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
