/**
 * 
 */
package com.dianping.tiger.biz.task.intercepter;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.dianping.tiger.biz.task.dao.DispatchTaskDao;
import com.dianping.tiger.biz.task.dataobject.TigerTaskDo;
import com.dianping.tiger.biz.task.monitor.MonitorDetailManager;
import com.dianping.tiger.biz.task.monitor.MonitorDetailManager.MonitorType;

/**
 * @author yuantengkai
 * 监控拦截
 */
public class MonitorDetailIntercepter implements MethodInterceptor{
	
	@Resource
	private DispatchTaskDao dispatchTaskDao;
	
	@Resource
	private MonitorDetailManager monitorDetailManager;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		String methodName = method.getName();
		if("addDispatchTask".equalsIgnoreCase(methodName)){
			return dealAddIntercepter(invocation);
		}
		if("updateTaskStatus".equalsIgnoreCase(methodName)){
			return dealUpdateIntercepter(invocation);
		}
		if("addRetryTimesAndExecuteTime".equalsIgnoreCase(methodName)){
			return dealNextRetryIntercepter(invocation);
		}
		if("addRetryTimesByFail".equalsIgnoreCase(methodName)){
			return dealFailRetryIntercepter(invocation);
		}
		if("cancelTaskById".equalsIgnoreCase(methodName)){
			return dealCancleByIdIntercepter(invocation);
		}
		if("cancelTaskByBizUniqueId".equalsIgnoreCase(methodName)){
			return dealCancleByBizIdIntercepter(invocation);
		}
		return invocation.proceed();
	}



	private Object dealAddIntercepter(MethodInvocation invocation) throws Throwable {
		Object result = invocation.proceed();
		Long taskId = (Long) result;
		if(taskId > 0){
			TigerTaskDo taskEntity = dispatchTaskDao.loadTaskById(taskId);
			monitorDetailManager.recordDetail(taskEntity, MonitorType.Add);
		}
		return result;
		
	}
	
	private Object dealUpdateIntercepter(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Long taskId = (Long) args[0];
		Integer status = (Integer) args[1];
		String hostName = (String) args[2];
		String ttid = (String) args[3];
		Object result = invocation.proceed();
		TigerTaskDo taskEntity = dispatchTaskDao.loadTaskById(taskId);
		taskEntity.setStatus(status);
		taskEntity.setHost(hostName);
		taskEntity.setTtid(ttid);
		if(status == 1){
			monitorDetailManager.recordDetail(taskEntity, MonitorType.Success);
		}else{
			monitorDetailManager.recordDetail(taskEntity, MonitorType.Fail);
		}
		return result;
	}
	
	private Object dealNextRetryIntercepter(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Long taskId = (Long) args[0];
		Object result = invocation.proceed();
		TigerTaskDo taskEntity = dispatchTaskDao.loadTaskById(taskId);
		monitorDetailManager.recordDetail(taskEntity, MonitorType.NextRetry);
		return result;
	}
	
	private Object dealFailRetryIntercepter(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Long taskId = (Long) args[0];
		Object result = invocation.proceed();
		TigerTaskDo taskEntity = dispatchTaskDao.loadTaskById(taskId);
		monitorDetailManager.recordDetail(taskEntity, MonitorType.FailRetry);
		return result;
	}
	
	private Object dealCancleByIdIntercepter(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		Long taskId = (Long) args[1];
		Object result = invocation.proceed();
		TigerTaskDo taskEntity = dispatchTaskDao.loadTaskById(taskId);
		monitorDetailManager.recordDetail(taskEntity, MonitorType.Cancel);
		return result;
	}

	private Object dealCancleByBizIdIntercepter(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		String bizUniqueId = (String) args[1];
		Object result = invocation.proceed();
		TigerTaskDo taskEntity = dispatchTaskDao.loadTaskByBizUniqueId(bizUniqueId);
		monitorDetailManager.recordDetail(taskEntity, MonitorType.Cancel);
		return result;
	}

	

}
