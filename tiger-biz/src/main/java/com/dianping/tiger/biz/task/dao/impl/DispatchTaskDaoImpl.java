package com.dianping.tiger.biz.task.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.tiger.biz.task.dao.DispatchTaskDao;
import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;

/**
 * 任务表操作dao
 * @author yuantengkai
 *
 */
public class DispatchTaskDaoImpl extends SqlMapClientDaoSupport implements DispatchTaskDao{

	@Override
	public long addDispatchTask(TigerTaskDo entity) {
		try {
			Long id = (Long) getSqlMapClient().insert("tigerTask.insert", entity);
			if(id == null){
				return 0;
			}
			return id;
		} catch (Throwable e) {
			throw new RuntimeException("sql insert happens Exception.",e);
		}
		
	}

	@Override
	public boolean updateTaskStatus(long id, int status, String hostName, String ttid) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", status);
        params.put("hostName", hostName);
        params.put("ttid", ttid);
        int num = getSqlMapClientTemplate().update("tigerTask.updateTaskStatus", params);
        if(num < 1){
        	return false;
        }
		return true;
	}

	@Override
	public boolean addRetryTimesAndExecuteTime(long id, Date nextExecuteTime,
			String hostName, String ttid) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("nextExecuteTime", nextExecuteTime);
        params.put("hostName", hostName);
        params.put("ttid", ttid);
        int num = getSqlMapClientTemplate().update("tigerTask.updateRetryTimesAndExecuteTime", params);
        if(num < 1){
        	return false;
        }
		return true;
	}
	
	@Override
	public boolean addRetryTimesByFail(long id, Date nextExecuteTime,
			String hostName, String ttid){
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("nextExecuteTime", nextExecuteTime);
        params.put("hostName", hostName);
        params.put("ttid", ttid);
        int num = getSqlMapClientTemplate().update("tigerTask.updateRetryTimesAndExecuteTime", params);
        if(num < 1){
        	return false;
        }
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TigerTaskDo> findDispatchTasksWithLimit(String handlerGroup, String handler,
			List<Integer> nodeList, int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        if(!StringUtils.isBlank(handler)){
        	param.put("handler", handler);
        }
        param.put("nodeList", nodeList);
        param.put("limit", limit);
        return getSqlMapClientTemplate().queryForList("tigerTask.queryTasksWithLimit", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TigerTaskDo> findDispatchTasksWithLimitByBackFetch(String handlerGroup,
			String handler, List<Integer> nodeList, int limit, long id) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        if(!StringUtils.isBlank(handler)){
        	param.put("handler", handler);
        }
        param.put("nodeList", nodeList);
        param.put("limit", limit);
        param.put("id", id);
        return getSqlMapClientTemplate().queryForList("tigerTask.queryTasksWithLimitByBackFetch", param);
	}
	
	@Override
	public TigerTaskDo loadTaskById(long id){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
        return (TigerTaskDo) getSqlMapClientTemplate().queryForObject("tigerTask.loadTaskById", param);
	}
	
	@Override
	public TigerTaskDo loadTaskByBizUniqueId(String bizUniqueId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("bizUniqueId", bizUniqueId);
        return (TigerTaskDo) getSqlMapClientTemplate().queryForObject("tigerTask.loadTaskByBizUniqueId", param);
	}

	@Override
	public boolean cancelTaskById(String handlerGroup, long id) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("handlerGroup", handlerGroup);
        params.put("id", id);
        params.put("status", 2);
        params.put("extendRemark", "#canceledByBiz");
        int num = getSqlMapClientTemplate().update("tigerTask.cancelTaskById", params);
        if(num < 1){
        	return false;
        }
		return true;
	}

	@Override
	public boolean cancelTaskByBizUniqueId(String handlerGroup,
			String bizUniqueId) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("handlerGroup", handlerGroup);
        params.put("bizUniqueId", bizUniqueId);
        params.put("status", 2);
        params.put("extendRemark", "#canceledByBiz");
        int num = getSqlMapClientTemplate().update("tigerTask.cancelTaskByBizUniqueId", params);
        if(num < 1){
        	return false;
        }
		return true;
	}

}
