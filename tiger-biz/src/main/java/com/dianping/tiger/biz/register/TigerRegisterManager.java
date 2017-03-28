/**
 * 
 */
package com.dianping.tiger.biz.register;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.tiger.api.register.TigerContext;
import com.dianping.tiger.biz.register.dao.TigerRegisterDao;
import com.dianping.tiger.biz.register.dataobject.TigerRegisterDo;

/**
 * @author yuantengkai
 * 集群注册管理
 */
public class TigerRegisterManager {
	
	private static final Logger logger = LoggerFactory.getLogger(TigerRegisterManager.class);

	
	/**
	 * 集群最新的注册版本,key:handlerGroup
	 */
	private ConcurrentMap<String, TigerContext> registerMap = new ConcurrentHashMap<String, TigerContext>();
	
	@Resource
	private TigerRegisterDao tigerRegisterDao;
	
	/**
	 * 初始化，启动一个线程轮询注册版本是否更新
	 */
	public void init(){
		Thread registerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						for(Entry<String, TigerContext> entry : registerMap.entrySet()){
							String handlerGroup = entry.getKey();
							TigerContext registerContext = entry.getValue();
							TigerRegisterDo currentRegister = tigerRegisterDao.loadRecentlyRegister(handlerGroup);
							if(currentRegister != null 
									&& StringUtils.equals(registerContext.getRegisterVersion(), 
											currentRegister.getRegisterVersion())){
								continue;
							} else {
								registerContext.setRegisterVersion(currentRegister.getRegisterVersion());
								registerContext.setRegisterTime(currentRegister.getRegisterTime());
								registerContext.setHostName(currentRegister.getHostName());
							}
						}
					} catch (Exception e) {
						logger.error("loop query tiger register Exception,"+registerMap, e);
					} finally{
						try {
							Thread.sleep(5 * 1000);
						} catch (InterruptedException e) {
							//ignore
						}
					}
				}
			}

		});
		registerThread.setDaemon(true);
		registerThread.setName("Tiger-Register-Thread");
		registerThread.start();
	}
	
	/**
	 * 注册集群
	 * 1. 先更新本地的local cache，以最新的时间搓的版本为准
	 * 2. 更新到db
	 * @param context
	 * @return
	 */
	public boolean register(TigerContext context){
		String handlerGroup = context.getHandlerGroup();
		String hostName = context.getHostName();
		TigerContext existContext = registerMap.putIfAbsent(handlerGroup, context);
		if(existContext != null){
			if((context.getRegisterTime() -2) > existContext.getRegisterTime()){
				existContext = context;
			}
		}
		TigerRegisterDo registerDo = tigerRegisterDao.loadRegister(handlerGroup, hostName);
		if(registerDo == null){
			registerDo = new TigerRegisterDo();
			registerDo.setHandlerGroup(handlerGroup);
			registerDo.setHostName(hostName);
			registerDo.setRegisterVersion(context.getRegisterVersion());
			registerDo.setRegisterTime(context.getRegisterTime());
			registerDo.setVersion(0);
			try{
				long id = tigerRegisterDao.addTigerRegister(registerDo);
				if(id > 1){
					return true;
				}
			}catch(Exception e){
				logger.error("tigerRegisterDao.addTigerRegister db exception,"+context, e);
			}
		} else {
			int num = tigerRegisterDao.updateRegisterWithVersion(context.getRegisterVersion(), 
					context.getRegisterTime(), handlerGroup,hostName, registerDo.getVersion());
			if(num > 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 所轮询的机器是否有效（和当前集群版本一致则有效）
	 * @param context
	 * @return
	 */
	public boolean isValid(TigerContext reqContext){
		String reqHandlerGroup = reqContext.getHandlerGroup();
		String reqVersion = reqContext.getRegisterVersion();
		TigerContext registerContext = registerMap.get(reqHandlerGroup);
		if(registerContext == null){
			TigerRegisterDo registerDo = tigerRegisterDao.loadRecentlyRegister(reqHandlerGroup);
			if(registerDo == null){
				return false;
			}
			registerContext = new TigerContext();
			registerContext.setHandlerGroup(registerDo.getHandlerGroup());
			registerContext.setHostName(registerDo.getHostName());
			registerContext.setRegisterVersion(registerDo.getRegisterVersion());
			registerContext.setRegisterTime(registerDo.getRegisterTime());
			TigerContext exist = registerMap.putIfAbsent(reqHandlerGroup, registerContext);
			if(exist != null){
				registerContext = exist;
			}
		}
		//轮询版本和注册版本一致(特殊情况: 如果是假死的机器, 且当前缓存版本没有被更新,假死是小概率事情，因为tiger本身会向zk发送心跳)
		if(StringUtils.equals(reqVersion, registerContext.getRegisterVersion())){
			return true;
		}
		//可能当前的map缓存的是老的版本, 比较注册时间
		if(reqContext.getRegisterTime()  > (registerContext.getRegisterTime() + 5)){
			TigerRegisterDo currentRegister = tigerRegisterDao.loadRecentlyRegister(reqHandlerGroup);
			if(currentRegister == null){
				return false;
			}
			if(StringUtils.equals(reqVersion, currentRegister.getRegisterVersion())){
				registerMap.put(reqHandlerGroup, reqContext);
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(DigestUtils.md5Hex("aa").length());
		System.out.println(DigestUtils.md5Hex("aaddfdfd"));
		System.out.println(System.currentTimeMillis()/1000);
	}

}
