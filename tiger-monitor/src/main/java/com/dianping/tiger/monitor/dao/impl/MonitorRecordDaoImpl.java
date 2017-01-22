/**
 * 
 */
package com.dianping.tiger.monitor.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dianping.tiger.monitor.component.PageSqlMapClientDaoSupport;
import com.dianping.tiger.monitor.dao.MonitorRecordDao;
import com.dianping.tiger.monitor.dataobject.TigerMonitorRecordDo;

/**
 * @author yuantengkai
 *
 */
public class MonitorRecordDaoImpl extends PageSqlMapClientDaoSupport<TigerMonitorRecordDo> implements MonitorRecordDao {

	@Override
	public long addMonitorRecord(TigerMonitorRecordDo recordDo) throws SQLException {
		Long id = (Long) getSqlMapClientTemplate().insert("tigerMonitorRecord.insert", recordDo);
		if(id == null){
			return 0;
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TigerMonitorRecordDo> queryMonitorRecords(
			String handlerGroup, String handlerName, Date monitorTimeFrom,
			Date monitorTimeTo) {
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        param.put("handlerName", handlerName);
        param.put("monitorTimeFrom", monitorTimeFrom);
        param.put("monitorTimeTo", monitorTimeTo);
        
        List<TigerMonitorRecordDo> resultMap = getSqlMapClientTemplate().queryForList("tigerMonitorRecord.queryMonitorRecords", param);
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryMonitorHandlers(String handlerGroup,Date monitorTimeFrom, Date monitorTimeTo){
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("handlerGroup", handlerGroup);
        param.put("monitorTimeFrom", monitorTimeFrom);
        param.put("monitorTimeTo", monitorTimeTo);
        List<String> handlerList = getSqlMapClientTemplate().queryForList("tigerMonitorRecord.queryMonitorHandlers", param);
        return handlerList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryMonitorGroups(Date monitorTimeFrom, Date monitorTimeTo){
		Map<String, Object> param = new HashMap<String, Object>();
        param.put("monitorTimeFrom", monitorTimeFrom);
        param.put("monitorTimeTo", monitorTimeTo);
        List<String> groupList = getSqlMapClientTemplate().queryForList("tigerMonitorRecord.queryMonitorGroups", param);
        return groupList;
	}

}
