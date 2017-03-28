/**
 * 
 */
package com.dianping.tiger.api.dispatch;

import java.util.Date;

import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.register.TigerContext;

/**
 * @author yuantengkai 任务操作接口,对外暴露
 */
public interface DispatchTaskService {
	
	/**
	 * 添加一条任务
	 * 
	 * @param taskEntity:其中handlerGroup,handler,loadbalance,earliestExecuteTime不能为空
	 * @return
	 */
	public long addDispatchTask(DispatchTaskEntity taskEntity);

	/**
	 * 根据任务id更新任务状态
	 * 
	 * @param taskId
	 * @param status
	 *            :see DispatchTaskService.TaskType
	 * @param attr
	 * @return
	 */
	public boolean updateTaskStatus(long taskId, int status, TaskAttribute attr);

	/**
	 * 增加重试次数,并设定下次执行时间
	 * 
	 * @param taskId
	 * @param nextExecuteTime
	 * @param attr
	 * @return
	 */
	public boolean addRetryTimesAndExecuteTime(long taskId,
			Date nextExecuteTime, TaskAttribute attr);
	
	/**
	 * 失败重试
	 * @param taskId
	 * @param nextExecuteTime
	 * @param attr
	 * @return
	 */
	public boolean addRetryTimesByFail(long taskId,Date nextExecuteTime, TaskAttribute attr);
	
	/**
	 * 取消一个任务
	 * @param handlerGroup
	 * @param attr
	 * @return
	 */
	public boolean removeDispatchTask(String handlerGroup, TaskAttribute attr);
	
	//================以下是注册相关========================
	/**
	 * 发起一个注册
	 * @param tigerContext
	 * @return
	 */
	public boolean registerTiger(TigerContext tigerContext);

}
