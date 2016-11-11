/**
 * 
 */
package com.dianping.tiger.core;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.dispatch.DispatchTaskService;
import com.dianping.tiger.engine.ScheduleServer;

/**
 * @author yuantengkai
 * 对外提供的新增一个tiger任务类
 */
public class TigerTaskUtil {
	

	private static DispatchTaskService dispatchTaskService;
	
	/**
	 * 添加一个任务
	 * @param handler 执行的handler
	 * @param executeTime 执行时间
	 * @param loadbalance 负载均衡参数
	 * @param paraMap 业务参数
	 * @return
	 */
	public static long addTask(String handler, Date executeTime, int loadbalance,
			Map<String, Object> paraMap){
		if(StringUtils.isBlank(handler)){
			return 0;
		}
		if(paraMap == null){
			paraMap = new HashMap<String, Object>();
		}
		if(executeTime == null){
			executeTime = new Date();
		}
		DispatchTaskEntity taskEntity = new DispatchTaskEntity();
		taskEntity.setHandlerGroup(ScheduleServer.getInstance().getHandlerGroup());
		taskEntity.setHandler(handler);
		taskEntity.setEarliestExecuteTime(executeTime);
		taskEntity.setLoadbalance(loadbalance);
		taskEntity.setParameter(JSON.toJSONString(paraMap));
		long id = dispatchTaskService.addDispatchTask(taskEntity);
		return id;
	}

	public void setDispatchTaskService(DispatchTaskService dispatchTaskService) {
		TigerTaskUtil.dispatchTaskService = dispatchTaskService;
	}
	
	

}
