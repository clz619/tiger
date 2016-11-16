package com.dianping.tiger.biz.task.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.dianping.tiger.api.dispatch.DispatchSingleService;
import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.dispatch.TaskAttribute;
import com.dianping.tiger.biz.task.dao.DispatchTaskDao;
import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;

/**
 * 统一捞取任务策略实现
 * @author yuantengkai
 *
 */
public class DispatchTaskSingleServiceImpl implements DispatchSingleService {
	
	private static final Logger logger = LoggerFactory.getLogger(DispatchTaskSingleServiceImpl.class);
	
	@Resource
	private DispatchTaskDao dispatchTaskDao;

	@Override
	public long addDispatchTask(DispatchTaskEntity taskEntity) {
		if(taskEntity == null || StringUtils.isBlank(taskEntity.getHandler()) ||
				StringUtils.isBlank(taskEntity.getHandlerGroup())){
			return 0;
		}
		if(taskEntity.getNode() == null || taskEntity.getStatus() == null){
			logger.warn("illegal taskEntity arguments,maybe not request from tiger-client,"+taskEntity);
			return 0;
		}
		TigerTaskDo entity = new TigerTaskDo();
		entity.setHandlerGroup(taskEntity.getHandlerGroup());
		entity.setHandler(taskEntity.getHandler());
		entity.setNode(taskEntity.getNode());
		entity.setRetryTimes(0);
		entity.setStatus(taskEntity.getStatus());
		entity.setTtid(taskEntity.getTtid());
		if(taskEntity.getEarliestExecuteTime() == null){
			entity.setEarliestExecuteTime(new Date());
		}else{
			entity.setEarliestExecuteTime(taskEntity.getEarliestExecuteTime());
		}
		if(StringUtils.isBlank(taskEntity.getParameter())){
			Map<String,String> param = new HashMap<String,String>();
			entity.setParameter(JSON.toJSONString(param));
		}else{
			entity.setParameter(taskEntity.getParameter());
		}
		try {
			long id = dispatchTaskDao.addDispatchTask(entity);
			return id;
		} catch (Exception e) {
			logger.error("dao insert dispatchTask exception,"+entity,e);
		}
		return 0;
	}

	@Override
	public boolean updateTaskStatus(long taskId, int status, TaskAttribute attr) {
		if(taskId < 1 || attr == null){
			return false;
		}
		return dispatchTaskDao.updateTaskStatus(taskId, status, attr.getHostName(), attr.getTtid());
	}

	@Override
	public boolean addRetryTimesAndExecuteTime(long taskId,
			Date nextExecuteTime, TaskAttribute attr) {
		if(taskId < 1 || nextExecuteTime == null || attr == null){
			return false;
		}
		return dispatchTaskDao.addRetryTimesAndExecuteTime(taskId, nextExecuteTime, 
						attr.getHostName(), attr.getTtid());
	}

	@Override
	public List<DispatchTaskEntity> findDispatchTasksWithLimit(
			String handlerGroup, List<Integer> nodeList, int limit) {
		if(StringUtils.isBlank(handlerGroup) || nodeList == null || nodeList.size() == 0){
			return null;
		}
		List<TigerTaskDo> doList = dispatchTaskDao.findDispatchTasksWithLimit(handlerGroup, null, nodeList, limit);
		if(doList == null || doList.size() == 0){
			return null;
		}
		List<DispatchTaskEntity> taskList = new ArrayList<DispatchTaskEntity>();
		for(TigerTaskDo tt:doList){
			DispatchTaskEntity task = new DispatchTaskEntity();
			BeanUtils.copyProperties(tt, task);
			taskList.add(task);
		}
		return taskList;
	}

	@Override
	public List<DispatchTaskEntity> findDispatchTasksWithLimitByBackFetch(
			String handlerGroup, List<Integer> nodeList, int limit, long taskId) {
		if(StringUtils.isBlank(handlerGroup) || nodeList == null 
				|| nodeList.size() == 0 || taskId < 1){
			return null;
		}
		List<TigerTaskDo> doList = dispatchTaskDao.findDispatchTasksWithLimitByBackFetch(handlerGroup, null, nodeList, limit, taskId);
		if(doList == null || doList.size() == 0){
			return null;
		}
		List<DispatchTaskEntity> taskList = new ArrayList<DispatchTaskEntity>();
		for(TigerTaskDo tt:doList){
			DispatchTaskEntity task = new DispatchTaskEntity();
			BeanUtils.copyProperties(tt, task);
			taskList.add(task);
		}
		return taskList;
	}
	
	@Override
	public boolean removeDispatchTask(String handlerGroup, TaskAttribute attr) {
		if(StringUtils.isBlank(handlerGroup) || attr == null){
			return false;
		}
		if(attr.getTaskId() > 0){
			return dispatchTaskDao.cancelTaskById(handlerGroup, attr.getTaskId());
		}
		if(!StringUtils.isBlank(attr.getBizUniqueId())){
			return dispatchTaskDao.cancelTaskByBizUniqueId(handlerGroup, attr.getBizUniqueId());
		}
		return false;
	}

}
