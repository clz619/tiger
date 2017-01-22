/**
 * 
 */
package com.dianping.tiger.biz.task.dao;

import java.util.Date;
import java.util.List;

import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;

/**
 * @author yuantengkai
 *
 */
public interface DispatchTaskDao {
	
	
public long addDispatchTask(TigerTaskDo entity);
	
	public boolean updateTaskStatus(long id, int status, String hostName, String ttid);

	public boolean addRetryTimesAndExecuteTime(long id,Date nextExecuteTime, String hostName, String ttid);

	public boolean addRetryTimesByFail(long id, Date nextExecuteTime, String hostName, String ttid);
	
	public List<TigerTaskDo> findDispatchTasksWithLimit(String handlerGroup, String handler,List<Integer> nodeList, int limit);

	public List<TigerTaskDo> findDispatchTasksWithLimitByBackFetch(String handlerGroup, String handler, List<Integer> nodeList, int limit, long id);
	
	public TigerTaskDo loadTaskById(long id);
	
	public TigerTaskDo loadTaskByBizUniqueId(String bizUniqueId);
	
	public boolean cancelTaskById(String handlerGroup, long id);
	
	public boolean cancelTaskByBizUniqueId(String handlerGroup, String bizUniqueId);
}
