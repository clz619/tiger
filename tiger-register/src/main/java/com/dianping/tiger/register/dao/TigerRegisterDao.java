/**
 * 
 */
package com.dianping.tiger.register.dao;

import java.util.List;

import com.dianping.tiger.register.dataobject.TigerRegisterDo;

/**
 * @author yuantengkai
 *
 */
public interface TigerRegisterDao {
	
	public long addTigerRegister(TigerRegisterDo entity);
	
	public int updateRegisterWithVersion(String destRegisterVersion, long registerTime,
					String nodes, String handlerGroup, String hostName,long expectVersion);
	
	public TigerRegisterDo loadRecentlyRegister(String handlerGroup);
	
	public TigerRegisterDo loadRegister(String handlerGroup, String hostName);
	
	public List<TigerRegisterDo> queryRegisterInfos(String handlerGroup);

}
