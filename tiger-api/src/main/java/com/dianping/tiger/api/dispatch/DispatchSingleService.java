/**
 * 
 */
package com.dianping.tiger.api.dispatch;

import java.util.List;

/**
 * @author yuantengkai
 * 统一捞取任务，再根据handler分发
 */
public interface DispatchSingleService extends DispatchTaskService {

	/**
	 * 获取一定数量的任务
	 * 
	 * @param handlerGroup
	 *            任务组名称
	 * @param nodeList
	 *            任务节点
	 * @param limit
	 *            任务上限数
	 * @return
	 */
	public List<DispatchTaskEntity> findDispatchTasksWithLimit(String handlerGroup,
			List<Integer> nodeList, int limit);

	/**
	 * 反压获取一定数量的任务
	 * 
	 * @param handlerGroup
	 * @param nodeList
	 * @param limit
	 * @param taskId
	 * @return
	 */
	public List<DispatchTaskEntity> findDispatchTasksWithLimitByBackFetch(String handlerGroup,
			List<Integer> nodeList, int limit, long taskId);

}
