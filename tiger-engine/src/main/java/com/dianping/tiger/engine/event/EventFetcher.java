/**
 * 
 */
package com.dianping.tiger.engine.event;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dianping.tiger.api.dispatch.DispatchMultiService;
import com.dianping.tiger.api.dispatch.DispatchSingleService;
import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.dispatch.DispatchTaskService;
import com.dianping.tiger.api.register.TigerContext;
import com.dianping.tiger.engine.ScheduleServer;
import com.dianping.tiger.engine.utils.ScheduleConstants;

/**
 * @author yuantengkai 任务捞取类
 */
public class EventFetcher {

	public static final int TASK_NUM = 200;

	private DispatchTaskService dispatchTaskService;

	public EventFetcher(DispatchTaskService dispatchTaskService) {
		this.dispatchTaskService = dispatchTaskService;
	}

	/**
	 * 获取对应的任务
	 * 
	 * @param handlerName
	 *            执行器名称
	 * @param nodeList
	 *            执行器节点
	 * @return
	 */
	public List<DispatchTaskEntity> getTasks(String handlerName,
			List<Integer> nodeList) {
		TigerContext tigerContext = new TigerContext();
		tigerContext.setHandlerGroup(ScheduleServer.getInstance().getHandlerGroup());
		tigerContext.setHostName(ScheduleServer.getInstance().getServerName());
		tigerContext.setRegisterVersion(ScheduleServer.getInstance().getRegisterVersion());
		tigerContext.setRegisterTime(ScheduleServer.getInstance().getRegisterTime());
		if (ScheduleServer.getInstance().getTaskStrategy() == ScheduleConstants.TaskFetchStrategy.Multi
				.getValue()) {// 各个执行器捞取策略
			if (StringUtils.isBlank(handlerName) || nodeList == null
					|| nodeList.size() == 0) {
				throw new IllegalArgumentException(
						"handlerName or nodeList is empty.");
			}
			DispatchMultiService dispatchMultiService = (DispatchMultiService) dispatchTaskService;
			List<DispatchTaskEntity> tasks = dispatchMultiService
					.findDispatchTasksWithLimit(ScheduleServer.getInstance().getHandlerGroup(), 
												handlerName, nodeList, TASK_NUM,tigerContext);
			if (tasks == null) {
				return Collections.emptyList();
			}
			return tasks;
		}
		//单个执行器统一捞取策略
		if (nodeList == null || nodeList.size() == 0) {
			throw new IllegalArgumentException("nodeList is empty.");
		}
		DispatchSingleService dispatchSingleService = (DispatchSingleService) dispatchTaskService;
		List<DispatchTaskEntity> tasks = dispatchSingleService
				.findDispatchTasksWithLimit(ScheduleServer.getInstance().getHandlerGroup(),nodeList, TASK_NUM,tigerContext);
		if (tasks == null) {
			return Collections.emptyList();
		}
		return tasks;
	}

	/**
	 * 反压获取任务名称
	 * 
	 * @param handlerName
	 * @param nodeList
	 * @param taskId
	 * @return
	 */
	public List<DispatchTaskEntity> getTasksByBackFetch(String handlerName,
			List<Integer> nodeList, long taskId) {
		TigerContext tigerContext = new TigerContext();
		tigerContext.setHandlerGroup(ScheduleServer.getInstance().getHandlerGroup());
		tigerContext.setHostName(ScheduleServer.getInstance().getServerName());
		tigerContext.setRegisterVersion(ScheduleServer.getInstance().getRegisterVersion());
		tigerContext.setRegisterTime(ScheduleServer.getInstance().getRegisterTime());
		if (ScheduleServer.getInstance().getTaskStrategy() == ScheduleConstants.TaskFetchStrategy.Multi
				.getValue()) {// 各个执行器捞取策略
			if (StringUtils.isBlank(handlerName) || nodeList == null
					|| nodeList.size() == 0 || taskId < 1) {
				throw new IllegalArgumentException(
						"backFetch task,handlerName or nodeList is empty,or taskId smaller than 1");
			}
			DispatchMultiService dispatchMultiService = (DispatchMultiService) dispatchTaskService;
			List<DispatchTaskEntity> tasks = dispatchMultiService
					.findDispatchTasksWithLimitByBackFetch(ScheduleServer.getInstance().getHandlerGroup(),
							handlerName, nodeList, TASK_NUM / 2, taskId,tigerContext);
			if (tasks == null) {
				return Collections.emptyList();
			}
			return tasks;
		}
		//单个执行器统一捞取策略
		if (nodeList == null || nodeList.size() == 0 || taskId < 1) {
			throw new IllegalArgumentException(
					"backFetch task, nodeList is empty,or taskId smaller than 1");
		}
		DispatchSingleService dispatchSingleService = (DispatchSingleService) dispatchTaskService;
		List<DispatchTaskEntity> tasks = dispatchSingleService
				.findDispatchTasksWithLimitByBackFetch(ScheduleServer.getInstance().getHandlerGroup(),
						nodeList, TASK_NUM / 2, taskId,tigerContext);
		if (tasks == null) {
			return Collections.emptyList();
		}
		return tasks;
	}

}
