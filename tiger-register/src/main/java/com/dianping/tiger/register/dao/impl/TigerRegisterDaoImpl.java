/**
 * 
 */
package com.dianping.tiger.register.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.dianping.tiger.register.dataobject.TigerRegisterDo;
import com.dianping.tiger.register.dao.TigerRegisterDao;

/**
 * @author yuantengkai
 *
 */
public class TigerRegisterDaoImpl extends SqlMapClientDaoSupport implements TigerRegisterDao{

	@Override
	public long addTigerRegister(TigerRegisterDo entity) {
		try {
			Long id = (Long) getSqlMapClient().insert("tigerRegister.insert", entity);
			if(id == null){
				return 0;
			}
			return id;
		} catch (Throwable e) {
			throw new RuntimeException("sql insert happens Exception.",e);
		}
	}

	@Override
	public int updateRegisterWithVersion(String destRegisterVersion,
			long registerTime, String nodes, String handlerGroup, String hostName,
			long expectVersion) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("registerVersion", destRegisterVersion);
        params.put("registerTime", registerTime);
        params.put("nodes", nodes);
        params.put("handlerGroup", handlerGroup);
        params.put("hostName", hostName);
        params.put("expectVersion", expectVersion);
        return getSqlMapClientTemplate().update("tigerRegister.updateRegisterVersion", params);
        
	}

	@Override
	public TigerRegisterDo loadRecentlyRegister(String handlerGroup) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("handlerGroup", handlerGroup);
        return (TigerRegisterDo) getSqlMapClientTemplate().queryForObject(
        			"tigerRegister.loadRecentlyRegisterByHandlerGroup", params);
	}

	@Override
	public TigerRegisterDo loadRegister(String handlerGroup, String hostName) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("handlerGroup", handlerGroup);
        params.put("hostName", hostName);
        return (TigerRegisterDo) getSqlMapClientTemplate().queryForObject(
        			"tigerRegister.loadRegisterByHandlerGroupAndName", params);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TigerRegisterDo> queryRegisterInfos(String handlerGroup){
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("handlerGroup", handlerGroup);
        return getSqlMapClientTemplate().queryForList(
        			"tigerRegister.queryRegistersByHandlerGroup", params);
	}

}
