/**
 * 
 */
package com.dianping.tiger.engine.dispatch;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuantengkai
 * 任务分发参数类
 */
public class DispatchParam {

	private Map<String, Object> properties = new HashMap<String, Object>();

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	/**
	 * 业务参数 json格式
	 * @return json格式,可以通过com.alibaba.fastjson.JSON.parse(str)转化为map对象
	 */
	public String getBizParameter(){
		return (String) this.getProperty("param");
	}
	
	/**
	 * 获取任务的重试次数
	 * @return
	 */
	public Integer getRetryTimes(){
		return (Integer) this.getProperty("retryTimes");
	}
	
	/**
	 * 获取任务id
	 * @return
	 */
	public Long getTaskId(){
		return (Long) this.getProperty("id");
	}
	
	/**
	 * 获取traceId
	 * @return
	 */
	public String getTraceId(){
		return (String) this.getProperty("ttid");
	}
	
	/**
	 * 获取任务组
	 * @return
	 */
	public String getHandlerGroup(){
		return (String) this.getProperty("handlerGroup");
	}

}
