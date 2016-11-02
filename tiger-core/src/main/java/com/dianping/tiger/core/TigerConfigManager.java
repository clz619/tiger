/**
 * 
 */
package com.dianping.tiger.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dianping.tiger.engine.ScheduleManagerFactory;
import com.dianping.tiger.engine.utils.ScheduleConstants;

/**
 * @author yuantengkai
 * 注册TigerConfigManager,等server启动后，启动tiger;配置参考如下:<br/>
 * <!-- 注册一个tigerConfigManager -->
	<bean id="tigerConfigManager" class="com.dianping.tiger.core.TigerConfigManager">
		<!-- 必须,设置20s轮询一次任务 -->
		<property name="interval" value="20"/>
		<!-- 必须,设置zk集群地址 -->
		<property name="zkConnectAddress" value="127.0.0.1:2181,10.25.13.11:2181"/>
		<!-- 必须,设置任务组名，要保证唯一，最好使用应用名称 -->
		<property name="handlerGroup" value="weddingTiger"/>
		<!-- 必须,设置需要执行的任务,支持动态调整 -->
		<property name="handlers" value="demoHandler1,demoHandler2"/>
		<!-- 设置虚拟节点个数，个数需至少大于集群机器数,默认100 -->
		<property name="virtualNodeNum" value="50"/>
		<!-- 设置zk虚拟节点分配策略,0-散列模式,1－分块模式,默认分块模式 -->
		<property name="divideType" value="1"/>
		<!-- 设置执行器策略，0-统一捞取任务策略；1-各个执行器各自捞取任务策略，默认为1 -->
		<property name="taskStrategy" value="1"/>
		<!-- 设置最小核心线程数，默认为2；taskStrategy为0时设置稍微大一点，比如10-->
		<property name="ThreadCoreSize" value="5"/>
		<!-- 设置最大核心线程数，默认为5；taskStrategy为0时设置稍微大一点，比如20-->
		<property name="ThreadMaxSize" value="10"/>
		<!-- 设置总调度开关,默认true,支持动态调整 -->
		<property name="scheduleFlag" value="true"/>
		<!-- 设置启用巡航模式，默认true,支持动态调整 -->
		<property name="enableNavigate" value="true"/>
		<!-- 设置启用反压模式，默认false,支持动态调整 -->
		<property name="enableBackFetch" value="false"/>
	</bean>
 */
public class TigerConfigManager implements InitializingBean,ApplicationContextAware{
	
	private	ScheduleManagerFactory smf;
	
	private Properties configp = new Properties();
	
	/**
	 * 轮询间隔，单位s
	 */
	private int interval = 20;
	
	private ApplicationContext applicationcontext;
	
	private AtomicBoolean initFlag = new AtomicBoolean(false);

	@Override
	public void afterPropertiesSet() throws Exception {
		startTiger();
	}

	public void startTiger() throws IllegalArgumentException, Exception {
		if (!initFlag.compareAndSet(false, true)) {
			return;
		}
		if (StringUtils.isBlank(configp.getProperty(ScheduleManagerFactory.ZookeeperKeys.zkConnectAddress.name()))) {
            throw new IllegalArgumentException("Property 'zkConnectAddress' is required");
        }
		if (StringUtils.isBlank(configp.getProperty(ScheduleManagerFactory.ScheduleKeys.handlerGroup.name()))) {
            throw new IllegalArgumentException("Property 'handlerGroup' is required");
        }
        if (StringUtils.isBlank(configp.getProperty(ScheduleManagerFactory.ScheduleKeys.handlers.name()))) {
            throw new IllegalArgumentException("Property 'handlers' is required");
        }
		
        if(interval < 3){
        	interval = 10;
        }
		smf = new ScheduleManagerFactory(interval * 1000);
		
		smf.setAppCtx(applicationcontext);
	
		//===========初始化启用==========
		smf.initSchedule(configp);
		initFlag.set(true);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationcontext = applicationContext;
	}

	/**
	 * 必须,轮询间隔
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	/**
	 * 必须,zk地址
	 * @param zkConnectAddress
	 */
	public void setZkConnectAddress(String zkConnectAddress){
		configp.setProperty(ScheduleManagerFactory.ZookeeperKeys.zkConnectAddress.name(),zkConnectAddress);
	}
	
	/**
	 * 必须,handler组，要保证唯一，最好使用应用名称
	 * @param handlerGroup
	 */
	public void setHandlerGroup(String handlerGroup){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.handlerGroup.name(),handlerGroup);
	}
	
	/**
	 * 必须,执行器名称,支持动态调整handler
	 * @param handlers
	 */
	public void setHandlers(String handlers){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.handlers.name(),handlers);
		if(initFlag.get()){
			List<String> handlerList = new ArrayList<String>();
			for (String h : handlers.split(",")) {
				handlerList.add(h);
			}
			smf.reSchedule(handlerList);
		}
	}
	
	/**
	 * 虚拟节点个数，必须，个数需至少大于集群机器数,默认100
	 * @param virtualNodeNum
	 */
	public void setVirtualNodeNum(int virtualNodeNum){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.virtualNodeNum.name(),virtualNodeNum+"");
	}
	
	/**
	 * zk虚拟节点分配策略,0-散列模式,1－分块模式,默认1
	 * @param divideType
	 */
	public void setDivideType(int divideType){
		if(divideType == 0){
			configp.setProperty(ScheduleManagerFactory.ScheduleKeys.divideType.name(), 
					ScheduleConstants.NodeDivideMode.DIVIDE_SANLIE_MODE.getValue()+"");
		}else{
			configp.setProperty(ScheduleManagerFactory.ScheduleKeys.divideType.name(), 
					ScheduleConstants.NodeDivideMode.DIVIDE_RANGE_MODE.getValue()+"");
		}
	}
	
	/**
	 * 执行器策略，0-统一捞取任务策略；1-各个执行器各自捞取任务策略，默认为1
	 * @param taskStrategy
	 */
	public void setTaskStrategy(int taskStrategy){
		if(taskStrategy == 0){
			configp.setProperty(ScheduleManagerFactory.ScheduleKeys.taskStrategy.name(),
					ScheduleConstants.TaskFetchStrategy.Single.getValue()+"");
		}else{
			configp.setProperty(ScheduleManagerFactory.ScheduleKeys.taskStrategy.name(),
					ScheduleConstants.TaskFetchStrategy.Multi.getValue()+"");

		}
	}
	
	/**
	 * 设置handler线程池的coreSize,<=maxSize<br/>
	 * tip:taskStrategy为0时，设置得稍微大一点，比如10
	 * @param coreSize  最小核心线程数
	 */
	public void setThreadCoreSize(int coreSize){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.coreSize.name(), coreSize+"");
	}
	
	/**
	 * 设置handler线程池的maxSize,>=coreSize<br/>
	 * tip:taskStrategy为0时，设置得稍微大一点，比如20
	 * @param maxSize 最大线程数
	 */
	public void setThreadMaxSize(int maxSize){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.maxSize.name(), maxSize+"");
	}
	
	/**
	 * 总调度开关,默认true,支持动态调整
	 * @param scheduleFlag
	 */
	public void setScheduleFlag(boolean scheduleFlag){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.scheduleFlag.name(),scheduleFlag+"");
		if(initFlag.get()){
			smf.setScheduleFlag(scheduleFlag);
		}
	}
	
	/**
	 * 启用巡航模式，默认true,支持动态调整
	 * @param enableNavigate
	 */
	public void setEnableNavigate(boolean enableNavigate){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.enableNavigate.name(),enableNavigate+"");
		if(initFlag.get()){
			smf.setNavigateFlag(enableNavigate);
		}
	}
	
	/**
	 * 启用反压模式，默认false,支持动态调整
	 * @param enableBackFetch
	 */
	public void setEnableBackFetch(boolean enableBackFetch){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.enableBackFetch.name(),enableBackFetch+"");
		if(initFlag.get()){
			smf.setBackFetchFlag(enableBackFetch);
		}
	}
	
	/**
	 * 启用groovy代码,默认false,支持动态调整
	 * @param enableGroovyCode
	 */
	public void setEnableGroovyCode(boolean enableGroovyCode){
		configp.setProperty(ScheduleManagerFactory.ScheduleKeys.enableGroovyCode.name(),enableGroovyCode+"");
		if(initFlag.get()){
			smf.setGroovyCodeFlag(enableGroovyCode);
		}
	}
	
	/**
	 * 启用监控,默认false;结合setMonitorIP()使用,支持动态调整
	 * @param enableMonitor
	 */
	public void setEnableMonitor(boolean enableMonitor){
		configp.setProperty(ScheduleManagerFactory.MonitorKeys.enableMonitor.name(),enableMonitor+"");
		if(initFlag.get()){
			smf.setMonitorFlag(enableMonitor);
		}
	}
	
	/**
	 * 设置监控url
	 * @param monitorIP exm:http://10.3.14.36:8080
	 */
	public void setMonitorUrl(String monitorUrl){
		configp.setProperty(ScheduleManagerFactory.MonitorKeys.monitorIP.name(), monitorUrl);
	}

	/**
	 * 设置个别handler的个性化开关配置,需要等tiger启动完才能设置,支持动态调整
	 * @param handler
	 * @param configMap:
	 * 	 key:switchername, for example {@link ScheduleManagerFactory.ScheduleKeys.enableNavigate.name()}
	 *   value:boolean
	 */
	public void setHandlerConfig(String handler, HashMap<String,Boolean> configMap){
		if(initFlag.get()){
			smf.setHandlerConfig(handler, configMap);
		}
	}
}
