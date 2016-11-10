/**
 * 
 */
package com.dianping.tiger.biz.task.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;

/**
 * @author yuantengkai
 *
 */
public interface DispatchTaskDao {
	
	
	public long addDispatchTask(TigerTaskDo entity) throws SQLException;
	
	public boolean updateTaskStatus(long id, int status, String hostName, String ttid);

	public boolean addRetryTimesAndExecuteTime(long id,Date nextExecuteTime, String hostName, String ttid);

	public List<TigerTaskDo> findDispatchTasksWithLimit(String handlerGroup, String handler,List<Integer> nodeList, int limit);

	public List<TigerTaskDo> findDispatchTasksWithLimitByBackFetch(String handlerGroup, String handler, List<Integer> nodeList, int limit, long id);
}
