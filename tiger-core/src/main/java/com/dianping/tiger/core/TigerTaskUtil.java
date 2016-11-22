/**
 * 
 */
package com.dianping.tiger.core;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.dispatch.DispatchTaskService;
import com.dianping.tiger.api.dispatch.TaskAttribute;
import com.dianping.tiger.engine.ScheduleServer;
import com.dianping.tiger.engine.utils.ThreadContext;

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
	 * @param params 业务参数,json格式,可空
	 * @param bizUniqueId 业务方任务全局唯一标示符号，可以为空,主要用于取消某个特定的任务,可空
	 * @return
	 */
	public static long addTask(String handler, Date executeTime, int loadbalance,
			String params, String bizUniqueId){
		if(StringUtils.isBlank(handler)){
			return 0;
		}
		
		if(executeTime == null){
			executeTime = new Date();
		}
		DispatchTaskEntity taskEntity = new DispatchTaskEntity();
		taskEntity.setHandlerGroup(ScheduleServer.getInstance().getHandlerGroup());
		taskEntity.setHandler(handler);
		taskEntity.setEarliestExecuteTime(executeTime);
		taskEntity.setLoadbalance(loadbalance);
		taskEntity.setParameter(params);
		taskEntity.setBizUniqueId(bizUniqueId);
		taskEntity.setTtid((String) ThreadContext.get("ttid"));
		return dispatchTaskService.addDispatchTask(taskEntity);
	}
	
	/**
	 * 设置任务下次执行
	 * @param taskId 任务id
	 * @param loadbalance 添加任务时设置的负责均衡参数
	 * @param nextExecuteTime 任务下次执行时间
	 * @return
	 */
	public static boolean executeAtNextTime(long taskId, int loadbalance, Date nextExecuteTime){
		if(taskId < 1 || nextExecuteTime == null){
			return false;
		}
		TaskAttribute attr = new TaskAttribute(ScheduleServer.getInstance().getServerName(), (String) ThreadContext.get("ttid"));
		attr.setNode(Math.abs(loadbalance) % ScheduleServer.getInstance().getNumOfVisualNode());
		return dispatchTaskService.addRetryTimesAndExecuteTime(taskId, nextExecuteTime, attr);
		
	}
	
	/**
	 * 设置任务下次执行
	 * @param taskId 任务id
	 * @param node 任务所在节点
	 * @param nextExecuteTime 任务下次执行时间
	 * @return
	 */
	public static boolean executeAtNextTimeWithNode(long taskId, int node, Date nextExecuteTime){
		if(taskId < 1 || nextExecuteTime == null){
			return false;
		}
		TaskAttribute attr = new TaskAttribute(ScheduleServer.getInstance().getServerName(), (String) ThreadContext.get("ttid"));
		attr.setNode(node);
		return dispatchTaskService.addRetryTimesAndExecuteTime(taskId, nextExecuteTime, attr);
		
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
