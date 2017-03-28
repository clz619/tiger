/**
 * 
 */
package com.dianping.tiger.api.dispatch;

import java.util.List;

import com.dianping.tiger.api.register.TigerContext;

/**
 * @author yuantengkai
 * 统一捞取任务，再根据handler分发
 */
public interface DispatchSingleService extends DispatchTaskService {

	/**
	 * 获取一定数量的任务
	 * see since v2.2.0
	 * @param handlerGroup
	 *            任务组名称
	 * @param nodeList
	 *            任务节点
	 * @param limit
	 *            任务上限数
	 * @return
	 */
	@Deprecated
	public List<DispatchTaskEntity> findDispatchTasksWithLimit(String handlerGroup,
			List<Integer> nodeList, int limit);

	/**
	 * 反压获取一定数量的任务
	 * see since v2.2.0
	 * @param handlerGroup
	 * @param nodeList
	 * @param limit
	 * @param taskId
	 * @return
	 */
	@Deprecated
	public List<DispatchTaskEntity> findDispatchTasksWithLimitByBackFetch(String handlerGroup,
			List<Integer> nodeList, int limit, long taskId);
	
	/**
	 * 获取一定数量的任务,会对当前的机器注册版本和集群版本进行校验 
	 * since v2.2.0
	 * @param handlerGroup 任务组名称
	 * @param nodeList 任务节点
	 * @param limit 任务上限数
	 * @param tigerContext 注册信息
	 * @return
	 */
	public List<DispatchTaskEntity> findDispatchTasksWithLimit(String handlerGroup,
			List<Integer> nodeList, int limit, TigerContext tigerContext);
	
	/**
	 * 反压获取一定数量的任务,会对当前的机器注册版本和集群版本进行校验
	 * since v2.2.0
	 * @param handlerGroup
	 * @param nodeList
	 * @param limit
	 * @param taskId
	 * @param tigerContext
	 * @return
	 */
	public List<DispatchTaskEntity> findDispatchTasksWithLimitByBackFetch(String handlerGroup,
			List<Integer> nodeList, int limit, long taskId, TigerContext tigerContext);

}
