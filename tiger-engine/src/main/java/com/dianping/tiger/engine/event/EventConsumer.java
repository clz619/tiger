/**
 * 
 */
package com.dianping.tiger.engine.event;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dianping.tiger.api.dispatch.DispatchTaskEntity;
import com.dianping.tiger.engine.ScheduleServer;
import com.dianping.tiger.engine.dispatch.DispatchHandler;
import com.dianping.tiger.engine.dispatch.DispatchParam;
import com.dianping.tiger.engine.dispatch.DispatchResult;
import com.dianping.tiger.engine.dispatch.DispatchResultManager;
import com.dianping.tiger.engine.monitor.EventMonitor;
import com.dianping.tiger.engine.repository.EventInConsumerRepository;
import com.dianping.tiger.engine.utils.ThreadContext;
import com.dianping.tiger.engine.utils.UniqueUtils;

/**
 * @author yuantengkai 任务消费
 */
public class EventConsumer implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(EventConsumer.class);
	

	private DispatchTaskEntity task;

	private DispatchHandler dispatchHandler;

	private DispatchResultManager dispatchResultManager;

	private EventInConsumerRepository eventInConsumerRepository;

	private int executorVersinoSnapshot;

	public EventConsumer(DispatchHandler dispatchHandler,
			DispatchResultManager dispatchResultManager,
			DispatchTaskEntity dispatchTaskEntity, int executorVersinoSnapshot) {
		this.dispatchHandler = dispatchHandler;
		this.task = dispatchTaskEntity;
		this.dispatchResultManager = dispatchResultManager;
		this.executorVersinoSnapshot = executorVersinoSnapshot;
		eventInConsumerRepository = EventInConsumerRepository.getInstance();
	}

	@Override
	public void run() {
		// 再次确认检查,任务已被消费或执行版本已发生变化,则立即返回
		ScheduleServer.getInstance().incrRunningTask();
		if (eventInConsumerRepository.get(task.getId()) == null
				|| executorVersinoSnapshot != EventExecutorManager
						.getInstance().getCurrentExecutorVersion()) {
			eventInConsumerRepository.remove(task.getId());
			ScheduleServer.getInstance().decrRunningTask();
			return;
		}
		DispatchResult result = DispatchResult.SUCCESS;
		
		try {
			ThreadContext.init();
			String ttid = task.getTtid();
			if(StringUtils.isBlank(ttid)){//说明是初次任务创建的第一次执行
				ttid = UniqueUtils.getUniqueId();
				task.setTtid(ttid);
			}
			ThreadContext.put("ttid", ttid);
			DispatchParam param = new DispatchParam();
			param.addProperty("id", task.getId());
			param.addProperty("traceId", ttid);
			param.addProperty("handlerGroup", task.getHandlerGroup());
			param.addProperty("retryTimes", task.getRetryTimes());
			param.addProperty("param", task.getParameter());
			long start = System.currentTimeMillis();
			result = dispatchHandler.invoke(param);
			int duration = (int) (System.currentTimeMillis() - start);
			if(ScheduleServer.getInstance().enableMonitor()){
				EventMonitor.getInstance().record(task.getHandler(), result == DispatchResult.FAIL2RETRY?0:1, duration);
			}
		} catch (Throwable t) {
			logger.error("dispatch invoke exception," + task, t);
			result = DispatchResult.FAIL2RETRY;
			if(ScheduleServer.getInstance().enableMonitor()){
				EventMonitor.getInstance().record(task.getHandler(), 0, 0);
			}
		} finally {
			eventInConsumerRepository.remove(task.getId());
			ScheduleServer.getInstance().decrRunningTask();
			dispatchResultManager.processDispatchResult(result,
					task);
			ThreadContext.destroy();
		}

	}

}
