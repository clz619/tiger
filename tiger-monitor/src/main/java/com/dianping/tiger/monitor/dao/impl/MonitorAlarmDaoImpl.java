/**
 * 
 */
package com.dianping.tiger.monitor.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.component.PageSqlMapClientDaoSupport;
import com.dianping.tiger.monitor.dao.MonitorAlarmDao;
import com.dianping.tiger.monitor.dataobject.TigerMonitorAlarmDo;

/**
 * @author yuantengkai
 *
 */
public class MonitorAlarmDaoImpl extends PageSqlMapClientDaoSupport<TigerMonitorAlarmDo> implements MonitorAlarmDao {

	
	@Override
	public long addMonitorAlarm(TigerMonitorAlarmDo alarmDo) throws SQLException{
		Long id = (Long) getSqlMapClientTemplate().insert("tigerMonitorAlarm.insert", alarmDo);
		if(id == null){
			return 0;
		}
		return id;
	}

	
	@Override
	public int updateAlarmInfo(long id, int leastFailNum, int intervalFailNum,
			String mailReceives, int offFlag) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("leastFailNum", leastFailNum);
        param.put("intervalFailNum", intervalFailNum);
        param.put("mailReceives", mailReceives);
        param.put("offFlag", offFlag);
        return getSqlMapClientTemplate().update("tigerMonitorAlarm.updateAlarmInfoById", param);
	}
	
	@Override
	public int deleteAlarmById(long id){
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        return getSqlMapClientTemplate().update("tigerMonitorAlarm.deleteAlarmById", param);
	}

	@Override
	public TigerMonitorAlarmDo loadAlarmByHandlerGroupAndName(String handlerGroup, String handlerName){
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        param.put("handlerName", handlerName);
        return (TigerMonitorAlarmDo) getSqlMapClientTemplate().queryForObject(
        		"tigerMonitorAlarm.loadAlarmByHandlerGroupAndName", param);
	}
	
	@Override
	public PageModel<TigerMonitorAlarmDo> pageQueryAlarms(int pageIndex, int pageSize, 
			String handlerGroup, String handlerName) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        param.put("handlerName", handlerName);
        return this.pageQuery(pageIndex, pageSize, param, 
        		"tigerMonitorAlarm.getAlarmCount", "tigerMonitorAlarm.getAlarmPageList");
	}

}
