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
import com.dianping.tiger.api.dispatch.TaskAttribute;
import com.dianping.tiger.engine.ScheduleServer;

/**
 * @author yuantengkai
 * 对外提供的tiger任务操作类
 */
public class TigerTaskUtil {
	

	private static DispatchTaskService dispatchTaskService;
	
	/**
	 * 添加一个任务
	 * @param handler 执行的handler
	 * @param executeTime 执行时间
	 * @param loadbalance 负载均衡参数
	 * @param paraMap 业务参数
	 * @param bizUniqueId 业务方任务全局唯一标示符号，可以为空,主要用于取消某个特定的任务
	 * @return
	 */
	public static long addTask(String handler, Date executeTime, int loadbalance,
			Map<String, Object> paraMap, String bizUniqueId){
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
		taskEntity.setBizUniqueId(bizUniqueId);
		return dispatchTaskService.addDispatchTask(taskEntity);
	}
	
	/**
	 * 根据任务id取消一个任务
	 * @param taskId
	 * @param loadbalance 添加任务时设置的负责均衡参数
	 * @return
	 */
	public static boolean removeTaskById(long taskId, int loadbalance){
		if(taskId < 1){
			return false;
		}
		TaskAttribute attr = new TaskAttribute();
		attr.setTaskId(taskId);
		attr.setNode(Math.abs(loadbalance) % ScheduleServer.getInstance().getNumOfVisualNode());
		return dispatchTaskService.removeDispatchTask(ScheduleServer.getInstance().getHandlerGroup(), 
														attr);
	}
	
	/**
	 * 根据业务自定义的业务标示符取消一个任务
	 * @param bizUniqueId
	 * @param loadbalance 添加任务时设置的负责均衡参数
	 * @return
	 */
	public static boolean removeTaskByBizUniqueId(String bizUniqueId,int loadbalance){
		if(StringUtils.isBlank(bizUniqueId)){
			return false;
		}
		TaskAttribute attr = new TaskAttribute();
		attr.setBizUniqueId(bizUniqueId);
		attr.setNode(Math.abs(loadbalance) % ScheduleServer.getInstance().getNumOfVisualNode());
		return dispatchTaskService.removeDispatchTask(ScheduleServer.getInstance().getHandlerGroup(), 
				attr);
	}

	public void setDispatchTaskService(DispatchTaskService dispatchTaskService) {
		TigerTaskUtil.dispatchTaskService = dispatchTaskService;
	}
	
	

}
