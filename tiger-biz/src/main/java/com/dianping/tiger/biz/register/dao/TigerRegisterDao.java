/**
 * 
 */
package com.dianping.tiger.biz.register.dao;

import com.dianping.tiger.biz.register.dataobject.TigerRegisterDo;

/**
 * @author yuantengkai
 *
 */
public interface TigerRegisterDao {
	
	public long addTigerRegister(TigerRegisterDo entity);
	
	public int updateRegisterWithVersion(String destRegisterVersion, long registerTime,
					String handlerGroup, String hostName,long expectVersion);
	
	public TigerRegisterDo loadRecentlyRegister(String handlerGroup);
	
	public TigerRegisterDo loadRegister(String handlerGroup, String hostName);

}
