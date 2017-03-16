/**
 * 
 */
package com.dianping.tiger.monitor.alarm;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.tiger.monitor.dataobject.TigerMonitorAlarmDo;
import com.dianping.tiger.monitor.service.MonitorService;
import com.dianping.tiger.monitor.vo.TigerDetailVo;


/**
 * @author yuantengkai
 * 监控报警管理类
 */
public class AlarmManager {
	
	private static final Logger logger = LoggerFactory
			.getLogger(AlarmManager.class);
	
	private final BlockingQueue<TigerDetailVo> alarmList = new LinkedBlockingQueue<TigerDetailVo>(20000);
	
	//key: handlerGroup_handlerName
	private final ConcurrentHashMap<String, TigerMonitorAlarmDo> alarmConfigCache = new ConcurrentHashMap<String, TigerMonitorAlarmDo>();
	
	//key: handlerGroup_handlerName
	private final ConcurrentHashMap<String, Long> alarmConfigValidCache = new ConcurrentHashMap<String, Long>();
	
	@Resource
	private MonitorService monitorService;
	
	@Resource
	private AlarmService dpAlarmService;
	
	
	public void init(){
		startDealAlarm();
	}
	
	
	/**
	 * 开启报警线程
	 */
	private void startDealAlarm() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						TigerDetailVo entity = alarmList.take();
						String key = entity.getHandlerGroup() + "_" + entity.getHandler();
						Long timestamp = alarmConfigValidCache.get(key);
						Long timestampNow = System.currentTimeMillis()/(2*60*1000);
						if(timestamp == null || timestamp < timestampNow){ //缓存失效或没有
							alarmConfigCache.remove(key);
							TigerMonitorAlarmDo alarmDo = monitorService.loadAlarmByHandlerGroupAndName(
										entity.getHandlerGroup(), entity.getHandler());
							if(alarmDo == null){
								alarmDo = new TigerMonitorAlarmDo();
							}
							alarmConfigCache.put(key, alarmDo);
							alarmConfigValidCache.put(key, timestampNow);
						}
						TigerMonitorAlarmDo alarm = alarmConfigCache.get(key);
						if(alarm != null && alarm.isOpen() && alarm.isAlarmTrigger(entity.getRetryTimes())){
							sendAlarm(entity, alarm.getMailReceives());
						}
						
					} catch(Throwable t){
						logger.error("Tiger-Monitor alarm deal exception.", t);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// ignore
						}
					}
				}
				
			}
		});
		t.setDaemon(true);
		t.setName("Tiger-Alarm-Thread");
		t.start();
	}

	/**
	 * 发送报警信息
	 * @param detail
	 */
	private void sendAlarm(TigerDetailVo detail, String mailReceives) {
		
		if(StringUtils.isBlank(mailReceives)){
			return;
		}
		String[] addressArr = mailReceives.split(";");
		String subject = "Tiger任务失败告警";
		StringBuilder sb = new StringBuilder();
		sb.append("任务组:").append(detail.getHandlerGroup()).append("\n");
		sb.append("handler:").append(detail.getHandler()).append("\n");
		sb.append("任务id:").append(detail.getTaskId()).append("\n");
		sb.append("执行结果:").append(detail.getExecuteResult()).append("\n");
		sb.append("连续失败次数:").append(detail.getRetryTimes()).append("\n");
		sb.append("任务参数:").append(detail.getParameter()).append("\n");
		sb.append("执行机器:").append(detail.getHost()).append("\n");
		dpAlarmService.sendMail(addressArr, subject, sb.toString());
	}

	
	/**
	 * 监控报警处理
	 * @param entity
	 */
	public void put2AlarmDeal(TigerDetailVo entity){
		alarmList.offer(entity);
	}
	
}
