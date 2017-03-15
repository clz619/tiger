package com.dianping.tiger.monitor.dao;

import java.sql.SQLException;

import com.dianping.tiger.monitor.component.PageModel;
import com.dianping.tiger.monitor.dataobject.TigerMonitorAlarmDo;

/**
 * 
 * @author yuantengkai
 *
 */
public interface MonitorAlarmDao {
	
	
	public long addMonitorAlarm(TigerMonitorAlarmDo alarmDo) throws SQLException;
	
	public int updateAlarmInfo(long id, int leastFailNum, int intervalFailNum,
				String mailReceives, int offFlag);
	
	public TigerMonitorAlarmDo loadAlarmByHandlerGroupAndName(String handlerGroup, String handlerName);
	
	public int deleteAlarmById(long id);

	public PageModel<TigerMonitorAlarmDo> pageQueryAlarms(int pageIndex, int pageSize, 
			String handlerGroup, String handlerName);
}
