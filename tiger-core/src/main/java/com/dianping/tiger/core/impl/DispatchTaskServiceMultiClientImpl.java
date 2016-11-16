/**
 * 
 */
package com.dianping.tiger.core.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dianping.tiger.api.dispatch.DispatchMultiService;
import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.api.dispatch.TaskAttribute;
import com.dianping.tiger.engine.ScheduleServer;
import com.dianping.tiger.engine.utils.ScheduleConstants;

/**
 * @author yuantengkai
 * 各个handler捞取各自任务的策略,client实现<br/>
 * 
 */
public class DispatchTaskServiceMultiClientImpl implements DispatchMultiService{
	
	private static final Logger logger = LoggerFactory.getLogger(DispatchTaskServiceMultiClientImpl.class);

	/**
	 * 单体模式下直接调用，微服务模式下改成rpc调用
	 */
	@Resource
	private DispatchMultiService dispatchTaskMultiBizService;
	
	@Override
	public long addDispatchTask(DispatchTaskEntity taskEntity) {
		if(taskEntity == null || StringUtils.isBlank(taskEntity.getHandler()) ||
				StringUtils.isBlank(taskEntity.getHandlerGroup())){
			return 0;
		}
		DispatchTaskEntity bizEntity = new DispatchTaskEntity();
		bizEntity.setHandlerGroup(taskEntity.getHandlerGroup());
		bizEntity.setHandler(taskEntity.getHandler());
		int loadbanlance = Math.abs(taskEntity.getLoadbalance());
		bizEntity.setNode(loadbanlance % ScheduleServer.getInstance().getNumOfVisualNode());
		bizEntity.setRetryTimes(0);
		bizEntity.setStatus(ScheduleConstants.TaskType.NEW.getValue());
		bizEntity.setTtid(taskEntity.getTtid());
		if(taskEntity.getEarliestExecuteTime() == null){
			bizEntity.setEarliestExecuteTime(new Date());
		}else{
			bizEntity.setEarliestExecuteTime(taskEntity.getEarliestExecuteTime());
		}
		if(StringUtils.isBlank(taskEntity.getParameter())){
			Map<String,String> param = new HashMap<String,String>();
			bizEntity.setParameter(JSON.toJSONString(param));
		}else{
			bizEntity.setParameter(taskEntity.getParameter());
		}
		try {
			long id = dispatchTaskMultiBizService.addDispatchTask(bizEntity);
			return id;
		} catch (Exception e) {
			logger.error("add dispatchTask exception,"+bizEntity,e);
		}
		return 0;
	}

	@Override
	public boolean updateTaskStatus(long taskId, int status, TaskAttribute attr) {
		if(taskId < 1 || attr == null){
			return false;
		}
		return dispatchTaskMultiBizService.updateTaskStatus(taskId, status, attr);
	}

	@Override
	public boolean addRetryTimesAndExecuteTime(long taskId,
			Date nextExecuteTime, TaskAttribute attr) {
		if(taskId < 1 || nextExecuteTime == null || attr == null){
			return false;
		}
		return dispatchTaskMultiBizService.addRetryTimesAndExecuteTime(taskId, nextExecuteTime, attr);
	}

	@Override
	public List<DispatchTaskEntity> findDispatchTasksWithLimit(String handlerGroup, String handler,
			List<Integer> nodeList, int limit) {
		if(StringUtils.isBlank(handler) || nodeList == null || nodeList.size() == 0){
			return null;
		}
		return dispatchTaskMultiBizService.findDispatchTasksWithLimit(handlerGroup,
				handler,nodeList,limit);
	}

	@Override
	public List<DispatchTaskEntity> findDispatchTasksWithLimitByBackFetch(String handlerGroup,
			String handler, List<Integer> nodeList, int limit, long taskId) {
		if(StringUtils.isBlank(handler) || nodeList == null 
				|| nodeList.size() == 0 || taskId < 1){
			return null;
		}
		return dispatchTaskMultiBizService.findDispatchTasksWithLimitByBackFetch(handlerGroup, handler, 
												nodeList, limit, taskId);
	}

	@Override
	public boolean removeDispatchTask(String handlerGroup, TaskAttribute attr) {
		if(StringUtils.isBlank(handlerGroup) || attr == null){
			return false;
		}
		return dispatchTaskMultiBizService.removeDispatchTask(handlerGroup, attr);
	}

}
